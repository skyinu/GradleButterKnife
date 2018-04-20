# GradleButterKnife

GradleButterKnife是一个Gradle Plugin,是[ButterKnife](https://github.com/JakeWharton/butterknife)用gradle的一种实现,也利用自定义注解完成了Android中view与field、method的绑定.

ButterKnife的实现方式是使用annotation processer在编译的过程中处理自定义注解来生成绑定代码,将视图绑定的模板代码生成到一个新的模板类中.GradleButterKnife开始的想法就是能不能不生成新的类完成数据绑定的过程,然后就像到用gradle transform在构建的时候修改需要绑定视图的class文件,直接在原class中插入新的方法从而避免生成模板类,也就是GradleButterKnife仅仅是将anntation的处理移到了gradle插件中进行实现.不过由于javaassit不支持再class文件中插入内部类,所以如果需要绑定方法则还是需要生成新的类.

在[这里](https://github.com/skyinu/RunMap)做过测试,将ButterKnife替换为GradleButterKnife

### 工程结构

工程结构基本都是参照ButterKnife的工程结构

+ buildSrc-核心代码完成了view与field、method绑定的代码生成
+ gradlebutterknife-annotations-自定义注解,其中的类基本copy自ButterKnife
+ gradlebutterknife-提供供外部调用一键绑定数据的api
+ sample、samplemoudle-使用示例,所有用法与ButerKnife的相同

### 用法

1.在项目根目录下的``build.gradle``中添加gradle插件的依赖

```
classpath 'com.skyinu:gradlebutterknife-plugin:0.1.2'
```

2.在app或者library工程中引入gradlebutterknife-annotations,gradlebutterknife依赖

```
implementation 'com.skyinu:gradlebutterknife-annotations:0.1.0'
implementation 'com.skyinu:gradlebutterknife:0.1.0'
```

3.在app或者library工程中apply插件,添加插件的配置代码

```
apply plugin: 'com.skyinu.gradlebutterkbife-plugin'

GradleButterKnife {
    dumpAble true //表示是否输出修改的.class文件
    dumpDir "${project.buildDir}${File.separator}dumpDir"//输出目录
}

```

