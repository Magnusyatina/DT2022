package org.magnusario

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.magnusario.tests.Application


class ApplicationTest {

    @Test
    void testModificationString() {
        String value = "aabbc"
        println "Testing value: $value"
        String result = Application.modifyString(value)
        println "Result value: $result"
        Assertions.assertEquals("c", result)

        value = "aab"
        println "Testing value: $value"
        result = Application.modifyString(value)
        println "Result value: $result"
        Assertions.assertEquals("b", result)

        value = "aabb"
        println "Testing value: $value"
        result = Application.modifyString(value)
        println "Result value: $result"
        Assertions.assertEquals("", result)

        value = "abfbaf"
        println "Testing value: $value"
        result = Application.modifyString(value)
        println "Result value: $result"
        Assertions.assertEquals("abfbaf", result)

        value = "abccbaf"
        println "Testing value: $value"
        result = Application.modifyString(value)
        println "Result value: $result"
        Assertions.assertEquals("f", result)
    }

    @Test
    void testModifyingNullValue() {
        println "Modifying null value"
        String value = Application.modifyString(null)
        Assertions.assertEquals(null, value)
    }

    @Test
    void testModifyingEmptyValue() {
        println "Modifying empty value"
        String value = ""
        String result = Application.modifyString(value)
        Assertions.assertEquals("", result)
    }
}
