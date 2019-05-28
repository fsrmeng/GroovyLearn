package objectorention

//默认都是public的
class Person implements Serializable {
    String name
    Integer age
    def increaceAge(int year) {
        return age + year
    }

    @Override
    Object invokeMethod(String s, Object o) {
        println "the method name is invokeMethod ${s}"
    }

    def methodMissing(String name, Object args) {
        println  "the method name is methodMissing ${name}"
    }
}

