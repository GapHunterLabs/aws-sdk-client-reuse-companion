package dev.gaphunter.awssdkclientreusecompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.awssdkclientreusecompanion.model.ClientBuildHit

/**
 * Finds `XxxClient.builder().build()` calls (AWS SDK for Java 2.x
 * service clients, [AwsSdkClientSignals]) written inside a
 * non-constructor method body -- AWS's own documentation states
 * service clients are thread-safe and meant to be created once and
 * reused; building one inside a regular method means a brand new
 * client (and its own connection pool) gets created on every call,
 * a real, documented performance footgun.
 *
 * **v0.1 scope, stated honestly:** only flags the direct
 * `.builder().build()` chain -- a builder assigned to an intermediate
 * variable before `.build()` is called isn't specially traced. Doesn't
 * flag a build call inside a constructor, a static initializer, or a
 * field initializer (all legitimate "create once" locations) --
 * matches by simple class name, so it works whether the real AWS SDK
 * jar is on the classpath or not.
 */
object JavaClientBuildFinder {

    fun findAll(file: PsiFile): List<ClientBuildHit> {
        val hits = mutableListOf<ClientBuildHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                super.visitMethodCallExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(buildCall: PsiMethodCallExpression): ClientBuildHit? {
        if (buildCall.methodExpression.referenceName != "build") return null

        val builderCall = buildCall.methodExpression.qualifierExpression as? PsiMethodCallExpression ?: return null
        if (builderCall.methodExpression.referenceName != "builder") return null

        val clientClassRef = builderCall.methodExpression.qualifierExpression as? PsiReferenceExpression ?: return null
        val clientClassName = clientClassRef.referenceName ?: return null
        if (clientClassName !in AwsSdkClientSignals.CLIENT_CLASS_NAMES) return null

        val containingMethod = PsiTreeUtil.getParentOfType(buildCall, PsiMethod::class.java) ?: return null
        if (containingMethod.isConstructor) return null

        return ClientBuildHit(leafOf(buildCall), clientClassName)
    }

    /** Descends to a real leaf PSI element -- LineMarkerInfo must never anchor on a composite node. */
    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
