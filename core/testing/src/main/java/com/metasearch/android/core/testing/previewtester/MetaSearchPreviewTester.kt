package com.metasearch.android.core.testing.previewtester

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.name
import com.metasearch.android.core.testing.rule.CoilRule
import org.junit.rules.RuleChain
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo

@OptIn(ExperimentalRoborazziApi::class)
class MetaSearchPreviewTester :
    ComposePreviewTester<ComposePreviewTester.TestParameter.JUnit4TestParameter<AndroidPreviewInfo>> {

    override fun test(testParameter: ComposePreviewTester.TestParameter.JUnit4TestParameter<AndroidPreviewInfo>) {
        val preview = testParameter.preview

        println("Capture Preview: ${preview.declaringClass.name} - ${preview.methodName}")

        try {
            testParameter.composeTestRule.setContent {
                preview()
            }

            testParameter.composeTestRule
                .onRoot()
                .captureRoboImage("${preview.declaringClass.name}_${preview.methodName}.png")
        } catch (e: Exception) {
            println("Error ${preview.methodName}: ${e.message}")
            throw e
        }
    }

    @Suppress("SpreadOperator")
    override fun testParameters(): List<ComposePreviewTester.TestParameter.JUnit4TestParameter<AndroidPreviewInfo>> {
        val options = options()
        return AndroidComposablePreviewScanner()
            .scanPackageTrees(*options.scanOptions.packages.toTypedArray())
            .let { scanner ->
                if (options.scanOptions.includePrivatePreviews) {
                    scanner.includePrivatePreviews()
                } else {
                    scanner
                }
            }
            .getPreviews()
            .map { previewItem ->
                ComposePreviewTester.TestParameter.JUnit4TestParameter(
                    composeTestRuleFactory = (
                        options.testLifecycleOptions as ComposePreviewTester.Options.JUnit4TestLifecycleOptions
                        ).composeRuleFactory,
                    preview = previewItem,
                )
            }
    }

    override fun options(): ComposePreviewTester.Options = super.options().copy(
        testLifecycleOptions = ComposePreviewTester.Options.JUnit4TestLifecycleOptions(
            testRuleFactory = { composeTestRule ->
                RuleChain.outerRule(CoilRule()).around(composeTestRule)
            },
        ),
    )
}
