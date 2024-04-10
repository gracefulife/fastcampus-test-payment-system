package com.example.fastcampuspaymentsystemlive

import spock.lang.Specification

class MathSpockTest extends Specification {

    def "Hello world"() {
        given:
        Integer a = 1;

        when:
        Boolean result = a == 1

        then:
        result
        println "Hello world"
    }
}
