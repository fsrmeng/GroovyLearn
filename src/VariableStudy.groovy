int x = 1
println(x.class)

def y = 3.14
println(y.class)

y = 'hello groovy'
println(y.class)

/* =================== 字符串的定义 ==============*/
def name = 'name one'
def doubleName = "name double"
//def tripleName = '''name triple'''
def tripleName = '''\
line one
line two
line three\
'''

println name.class
println doubleName.class
println tripleName

def sayHello = "Hello: ${name}" //可扩展做任意的表达式
println sayHello.class
println sayHello

/* ===================== 字符串的方法 =========== */
//字符串填充
//center
println "hello".center(10, 'abc')
//padLeft

//比较大小
//str > str2

//str[0]
//str[0..1]
//str.minus(str2) 或 str - str2

//str.reverse()
//str.capitalize() 首字母大写
//str.isNumber()

/* ================= 逻辑控制 ============== */
//switch .. case 可以是任意值
def n = 1.23
def result
switch (n) {
    case 'hello':
        result = 'find hello'
        break
    case 'groovy':
        result = 'find groovy'
        break
    case [1, 2, 3, 'hello groovy']:  //列表
        result = 'find list'
        break
    case 12..30:
        result = 'find range'    //范围
        break
    case Integer:
        result = 'find integer'
        break
    case BigDecimal:
        result = 'find bigDecimal'
        break
    default:
        result = 'find default'
        break
}
println result

//对范围的for循环
def sum = 0
for (i in 0..9) {
    sum += i
}

sum = 0
//对List的循环
for (i in [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]) {
    sum += i
}

sum = 0
//对Map进行循环
for (i in ['合肥':1, '杭州':2, '南京':3]) {
    sum += i.value
}

/* ================= 闭包 ============== */
//def clouser = {
//    println 'Hello groovy!'
//}
//clouser.call()
//clouser()

//def clouser = {String language ->
//    println "Hello $language"
//}

//def clouser = {
//    println "Hello ${it}"
//}
//clouser.call('groovy!')
//clouser('groovy!')

//闭包的返回值
//闭包一定是有返回值的，而且默认是最后一行
def clouser = {String language ->
    "Hello ${language}"
    'Hello Java!'
}
def word = clouser('groovy!')
println word

//闭包的使用
//int fun(int number) {
//    def result = 1
//    1.upto(number, {num -> result *= num})
//    return result
//}
//println fun(5)

int fun(int number) {
    def result = 1
    1.upto(number) {
//        num -> result *= num
        result *= it
    }
    return result
}

println fun(5)

//累加
int fun2(int number) {
    def result = 1
    number.times {
        num -> result += num
    }
    return result
}
println fun2(5)

/**
 * 字符串与闭包结合使用
 */
def str = 'the 2 add 3 is 5'
str.each {
    temp -> print temp
}

println()

//find函数查找符合条件的第一个
def finder = str.find {
    it.isNumber()
}
println finder

println str.findAll {
    it.isNumber()
}

println str.any {
    it.isNumber()
}

println str.every {
    it.isNumber()
}

println str.collect {
    it.toUpperCase()
}

/**
 * 闭包进阶
 * 闭包的三个重要变量：this, owner, delegate
 */
def scriptClouser = {
    println "scriptClouser this：" + this    //代表闭包定义处的类
    println "scriptClouser owner：" + owner  //代表闭包定义处的类或者对象
    println "scriptClouser delegate：" + delegate    //代表任意对象，默认与owner一致
}
//scriptClouser.call()

class Person {
    def classClouser = {
        println "classClouser this：" + this
        println "classClouser owner：" + owner
        println "classClouser delegate：" + delegate
    }

    def say() {
        def classClouser = {
            println "methodClouser this：" + this
            println "methodClouser owner：" + owner
            println "methodClouser delegate：" + delegate
        }
        classClouser.call()
    }
}

Person p = new Person()
p.classClouser.call()
p.say()

//闭包中定义一个闭包
def nestClouser = {
    def innerClouser = {
        println "innerClouser this：" + this
        println "innerClouser owner：" + owner
        println "innerClouser delegate：" + delegate
    }
    innerClouser.delegate = p //修改默认的delegate
    innerClouser.call()
}
//nestClouser.call()

//在类或者方法中定义闭包，那么this，owner，delegate都是一样的（默认没改变delegate）；
//如果在闭包中又嵌套一个闭包，那么this，owner，delegate将不再一样，this将指向闭包定义处外层的实例或者类本身（最接近的）
//而owner和delegate会指向最近的闭包对象；只有人为的修改delegate，owner和delegate才会不一样（this和owner是不可以修改的）

//闭包的委托策略
class Student {
    String name
    def pretty = {
        "my name is ${name}"
    }
}

class Teacher {
    String name
}

Student stu = new Student(name: "John")
Teacher tea = new Teacher(name: "jack")
stu.pretty.delegate = tea
stu.pretty.resolveStrategy = Closure.DELEGATE_FIRST
//println stu.pretty.call()

/* ================= 列表 ============== */
//def list = new ArrayList() //Java的
def list = [1, 2, 3, 4, 5]
println list.class
println list.size()
//def list2 = [1, 2, 3, 4, 5] as int[]
//println list2.class

//列表的增加
//list.add(5)
//list << 1
//println list

//列表的删除
//list.remove(1)
//list.removeElement(1)
//list.removeAll {
//    it % 2 != 0
//}
println list - [3]
println list

//列表的排序
def sortList = [4, -5, 9, -8, 7, -3, -2, 1, -6, -4]
sortList.sort {
    a, b -> Math.abs(a) >= Math.abs(b) ? 1 : -1
}
println sortList

def sortStringList = ['abc', 'z', 'John', 'Hello', 'zp']
sortStringList.sort {
    it.size()
}
println sortStringList

//列表的查找
def findList = [4, -5, 9, -8, 7, -3, -2, 1, -6, -4]
println findList.find {
    it % 2 != 0
}

println findList.findAll {
    it % 2 == 0
}

//打印结果为true
println findList.any {
    it % 2 == 0
}

//打印结果为false
println findList.every {
    it % 2 == 0
}

println findList.min {
    Math.abs(it)
}

//5
println findList.count {
    it % 2 == 0
}

/* ================= 映射 ============== */
def colors = [red: 'ff0000',
              green: '00ff00',
              blue: '0000ff']
println colors['red']
println colors.red

//添加元素
colors.black = 'fff'
println colors

colors.complex = [a: 1, b: 2]
println colors

def students = [
        1: [number: '0001', name: 'Bob',
            score: 55, sex: 'male'],
        2: [number: '0002', name: 'Johnny',
            score: 62, sex: 'female'],
        3: [number: '0003', name: 'Claire',
            score: 73, sex: 'female'],
        4: [number: '0004', name: 'Amy',
            score: 66, sex: 'male']
]

//遍历
students.each {student ->
//    println "key is ${student.key}, value is ${student.value}"
}

students.eachWithIndex{ student, index ->
//    println "index is ${index}, key is ${student.key}, value is ${student.value}"
}

//直接遍历key-value
students.each {key, value ->
//    println "key is ${key}, value is ${value}"
}

students.eachWithIndex{key, value, index ->
    println "index is ${index}, key is ${key}, value is ${value}"
}

//Map的查找
//find、findAll、count、groupBy
println students.groupBy {student ->
    student.value.score >= 60 ? "及格" : "不及格"
}

//Map的排序
println students.sort {student1, student2 ->
    Number score1 = student1.value.score as Number
    Number score2 = student2.value.score as Number
    score1 >= score2 ? 1 : -1
}

/* ================= 范围 ============== */
def ran = 1..10
println ran[1]

println ran.contains(0)
println ran.from
println ran.to

//遍历
ran.each {
    print "${it} "
}

//获取2-6（包括边界）之间的数
println ran.grep(2..6)

//switch..case
def getGradle(Number number) {
    switch (number) {
        case 0..<60:
            println '不及格'
            break
        case 60..70:
            println '及格'
            break
        case 70..80:
            println '良好'
            break
        case 80..100:
            println '优秀'
            break
    }
}
getGradle(90)