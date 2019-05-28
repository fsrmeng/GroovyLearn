package highlevel

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import objectorention.Person
import org.apache.tools.ant.taskdefs.Java
import org.testng.reporters.Buffer

import javax.net.ssl.HttpsURLConnection
import java.util.logging.Logger

/************* json解析 ************/
//将实体对象转换成Json字符串
def list = [new Person(name: 'Jhon', age: 25),
            new Person(name: 'Alice', age: 26)]
def json = JsonOutput.toJson(list)
println json
println JsonOutput.prettyPrint(json)

//将Json字符串转换成实体对象
JsonSlurper jsonSlurper = new JsonSlurper()
def parseList = jsonSlurper.parseText(json)
//println JsonOutput.prettyPrint(JsonOutput.toJson(parseList))

getNetworkData('https://home.firefoxchina.cn/?from=extra_start')

static def getNetworkData(String url) {
    def connection = new URL(url).openConnection() as HttpURLConnection
    connection.setRequestMethod('GET')
    connection.connect()
    def response = connection.content.text
//    println response
}

/************* xml解析 ************/
final String xml = '''
    <response version-api="2.0">
        <value>
            <books id="1" classification="android">
                <book available="20" id="1">
                    <title>疯狂Android讲义</title>
                    <author id="1">李刚</author>
                </book>
                <book available="14" id="2">
                   <title>第一行代码</title>
                   <author id="2">郭林</author>
               </book>
               <book available="13" id="3">
                   <title>Android开发艺术探索</title>
                   <author id="3">任玉刚</author>
               </book>
               <book available="5" id="4">
                   <title>Android源码设计模式</title>
                   <author id="4">何红辉</author>
               </book>
           </books>
           <books id="2" classification="web">
               <book available="10" id="1">
                   <title>Vue从入门到精通</title>
                   <author id="4">李刚</author>
               </book>
           </books>
       </value>
    </response>
'''
def xmlSlurper = new XmlSlurper()
def response = xmlSlurper.parseText(xml)
println response.value.books[0].book[0].title
println response.value.books[1].book.@available
println response.value.books[0].book[1].author

def listBook = []
response.value.books.each { books ->
    books.book.each { book ->
        if (book.author == '李刚') {
            listBook.add(book.title)
        }
    }
}
println listBook

//深度遍历xml数据，'**'可以替代depthFirst()
def listTitle = response.depthFirst().findAll { book ->
    book.author == '李刚'
}.collect { book ->
    book.title
}
println listTitle

//广度遍历xml数据
println response.value.books.children().findAll { node ->
    node.name() == 'book' && node.@id == '2'
}.collect { node ->
    node.title
}

/************* xml创建 ************/
/*
<langs type='current' count='3' mainstream='true'>
    <language flavor='static' version='1.5'>Java</language>
    <language flavor='dynamic' version='1.6'>Groovy</language>
    <language flavor='dynamic' version='1.9'>JavaScript</language>
</langs>
*/

def sw = new StringWriter()
def xmlBuilder = new MarkupBuilder(sw) //用来生成xml数据的核心类
xmlBuilder.langs(type: 'current', count: '3', mainstream: 'true') {
    language(flavor: 'static', version: '1.5', 'Java')
    language(flavor: 'dynamic', version: '1.6', 'Groovy')
    language(flavor: 'dynamic', version: '1.9', 'JavaScript')
}
println sw

def langs = new Langs()
xmlBuilder.langs(type: langs.type, count: langs.count, mainstream: langs.mainstream) {
    langs.languages.each { lang ->
        language(flavor: lang.flavor, version: lang.version, lang.value)
    }
}
println sw

class Langs {
    String type = 'current'
    int count = 3
    boolean mainstream = true
    def languages = [new language(flavor: 'static', version: 1.5, value: 'Java'),
                     new language(flavor: 'dynamic', version: 1.6, value: 'Groovy'),
                     new language(flavor: 'dynamic', version: 1.9, value: 'JavaScript')]
}

class language {
    String flavor
    float version
    String value
}

/************* 文件处理 ************/
//读取文件的整个内容
//第一种方式
def file = new File("../../HelloGroovy.iml")
file.eachLine {line ->
    println line
}

//第二种方式
println file.getText()

//第三种方式，返回的是一个集合
println file.readLines()

//读取文件部分内容
println file.withReader { reader ->
    def buffer = new char[100]
    reader.read(buffer)
    return buffer
}

//拷贝文件
def copy(String sourceFilePath, String destFilePath) {
    try {
        def destFile = new File(destFilePath)
        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        def sourceFile = new File(sourceFilePath)
        if (!sourceFile.exists()) {
            throw new Exception("source file is not exists!!")
        }

        sourceFile.withReader { reader ->
            def lines = reader.readLines()
            destFile.withWriter { write ->
                lines.each { line ->
                    write.append(line + "\r\n")
                }
            }
        }
//        def text = sourceFile.getText()
//        destFile.setText(text)
        return true
    } catch (Exception e) {
        e.printStackTrace()
    }
    return false
}

//println copy("../../HelloGroovy.iml", "../HelloGroovy2.iml")

//对象的写入
def writeObj(Object object, String destObjectPath) {
    try {
        def destFile = new File(destObjectPath)
        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        destFile.withObjectOutputStream {outputStream ->
            outputStream.writeObject(object)
        }
    } catch(Exception e) {
        e.printStackTrace()
    }
    return false
}

//对象的读取
def readObj(String sourceObjectPath) {
    def obj = null
    try {
        def sourceFile = new File(sourceObjectPath)
        if (!sourceFile.exists()) {
            return null
        }
        sourceFile.withObjectInputStream {inputStream ->
            obj = inputStream.readObject()
        }
    } catch(Exception e) {
        e.printStackTrace()
    }
    return obj
}

def person = new Person(name: 'zhangpan', age: 25)
//writeObj(person, '../../Person.bean')
def obj = readObj('../../Person.bean')
println "我的名字是：${((Person)obj).name}，我的年龄是：${((Person)obj).age}"