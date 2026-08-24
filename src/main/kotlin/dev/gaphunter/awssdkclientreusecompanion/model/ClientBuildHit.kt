package dev.gaphunter.awssdkclientreusecompanion.model

import com.intellij.psi.PsiElement

/** One AWS SDK v2 client `.builder().build()` call found inside a non-constructor method body. */
data class ClientBuildHit(val callElement: PsiElement, val clientClassName: String)
