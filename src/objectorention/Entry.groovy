package objectorention

class Entry {
    def static main(def args) {
        println '初始化应用...'
        ApplicationManager.init()
        println '初始化完成...'
        def person = PersonManager.createPerson("zhangpan", 25)
        println "person name is ${person.name}, age is ${person.age}"
    }
}
