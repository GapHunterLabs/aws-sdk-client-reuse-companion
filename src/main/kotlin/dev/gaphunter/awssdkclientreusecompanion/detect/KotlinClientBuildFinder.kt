package dev.gaphunter.awssdkclientreusecompanion.detect

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.awssdkclientreusecompanion.model.ClientBuildHit
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/** Kotlin counterpart of [JavaClientBuildFinder]. */
object KotlinClientBuildFinder {

    fun findAll(file: PsiFile): List<ClientBuildHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<ClientBuildHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
                super.visitDotQualifiedExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(buildExpr: KtDotQualifiedExpression): ClientBuildHit? {
        val buildCall = buildExpr.selectorExpression as? KtCallExpression ?: return null
        if (buildCall.calleeExpression?.text != "build") return null

        val builderExpr = buildExpr.receiverExpression as? KtDotQualifiedExpression ?: return null
        val builderCall = builderExpr.selectorExpression as? KtCallExpression ?: return null
        if (builderCall.calleeExpression?.text != "builder") return null

        val clientClassRef = builderExpr.receiverExpression as? KtNameReferenceExpression ?: return null
        val clientClassName = clientClassRef.getReferencedName()
        if (clientClassName !in AwsSdkClientSignals.CLIENT_CLASS_NAMES) return null

        // Not inside a constructor -- covers a class primary/secondary
        // constructor body, a legitimate "create once" location.
        if (PsiTreeUtil.getParentOfType(buildExpr, KtConstructor::class.java) != null) return null
        // A top-level/object property initializer (`val client = ...`)
        // outside any function is also a legitimate "create once"
        // location -- only flag when inside a real function body.
        if (PsiTreeUtil.getParentOfType(buildExpr, KtNamedFunction::class.java) == null) return null

        return ClientBuildHit(leafOf(buildExpr), clientClassName)
    }

    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
