package objectorention

def person = new Person(name: "zhangpan", age: 25)
//无论是直接用.去调用还是调用get/set最终都是去调用get/set
println "the name is ${person.name}, the age is ${person.age}"
println person.increaceAge(10)
person.cry()

//为类动态的添加方法
person.metaClass.cry = {
    println "the metaClass cry generated"
}
person.cry()

//为类动态的添加属性
person.metaClass.sex = 'male'
println person.sex