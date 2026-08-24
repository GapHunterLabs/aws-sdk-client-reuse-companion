package dev.gaphunter.awssdkclientreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaClientBuildFinderTest : BasePlatformTestCase() {

    fun `test a client built inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                void process() {
                    S3Client client = S3Client.builder().build();
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaClientBuildFinder.findAll(file).size)
    }

    fun `test a client built inside a constructor is not flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                private final S3Client client;
                OrderService() {
                    client = S3Client.builder().build();
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaClientBuildFinder.findAll(file).isEmpty())
    }

    fun `test an unrelated builder call is never flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                void process() {
                    Order order = Order.builder().build();
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaClientBuildFinder.findAll(file).isEmpty())
    }
}
