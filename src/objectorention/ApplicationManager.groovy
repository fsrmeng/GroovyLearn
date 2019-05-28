package objectorention

class ApplicationManager {
    static void init() {
        ExpandoMetaClass.enableGlobally()
        //为第三方类添加方法
        Person.metaClass.static.createPerosn = {String name, int age ->
            new Person(name: name, age: age)
        }
    }
}
