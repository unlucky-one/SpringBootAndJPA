# SpringBootAndJPA
&emsp;&emsp;使用JPA搭建一套极简代码的服务项目，适合有一定基础和对JPA有兴趣的朋友。
<br>
&emsp;&emsp;<a href="#1">基础篇</a>
<br>
&emsp;&emsp;<a href="#2">改造篇</a>
<br>
&emsp;&emsp;<a href="#3">技巧篇</a>
<br>
&emsp;&emsp;搭建了一套Spring Boot的微服务，数据库操作使用JPA。<br>
&emsp;&emsp;主要是自定义了BaseRepository类，实现了一些自定义方法，尽量的缩减了重复代码，欢迎爱好者共同学习。<br>
### 前言<br>
&emsp;&emsp;项目中使用到了Lombok插件，请没安装的朋友自行下载安装。否则带有@Data、@NoArgsConstructor、
@AllArgsConstructor的类无法编译。<br>
### <a name="1">一、基础篇</a><br>
#### Spring基础，有SpringMVC基础的人略过。<br>
&emsp;&emsp;进入项目首先你要打开`application.properties`文件，根据个人情况修改为你的数据库配置。<br>
&emsp;&emsp;`spring.jpa.hibernate.ddl-auto`这个配置需要注意。这个配置的目的是控制jpa对数据库如何操作。<br>
当值为create的时候每次启动项目Jpa会根据项目内的实体映射生成表。建议表生成后或数据库中的表已存在时将参数改成
none或update。<br>
&emsp;&emsp;`MyResponseAdvice`类是一个全局的返回值控制器。用来统一返回数据的格式。我这里返回的为map类型，
数据包含status、message、data。<br>
&emsp;&emsp;`MyAppConfig`对项目对项目做了一些配置，修改拦截器拦截的请求在这个类中配置。<br>
&emsp;&emsp;`MyFilter`自定义过滤器，所有请求都会经过这个类，可以加一些计数器操作。<br>
&emsp;&emsp;`MyAppInterceptor`未忽略的请求都会被此类拦截，用户验证、权限验证都可以在这里做。<br>
&emsp;&emsp;`MyHttpSessionListener`自定义Session监听器，这里做了一个在线人数统计功能。<br>
#### Jpa类说明<br>
&emsp;&emsp;`I18nUtils`国际化配置。<br>
&emsp;&emsp;`BaseRepositoryFactoryBean`为更换jpa处理类的工厂类，下一篇会详细说明。<br>
&emsp;&emsp;`BeanTransformerAdapter`bean的适配器类。<br>
&emsp;&emsp;`BaseRepository`接口，定义了一些要自己实现的通用方法，目的是减少重复工作，还可以对jpa操作
做一些扩展，方便项目维护。`BaseRepositoryImpl`是对它的实现。<br>
&emsp;&emsp;其他的Repository类中列出了一些jpa使用技巧，在第三篇会说明，类中也有注释。<br>
&emsp;&emsp;`entity`下的类中标注了@Entity和@Table的类为要映射到表的实体类，其他为自定义的返回结果。<br>
### <a name="2">二、改造篇</a><br>
&emsp;&emsp;项目主要对Jpa的基本Repository(SimpleJpaRepository)又做了一次封装。<br>
&emsp;&emsp;`BaseRepositoryFactoryBean`是一个工厂类，用来将jpa默认的处理类换成自定义的`BaseRepository`。
此类需要配置在程序的入口的@EnableJpaRepositories注解中的`repositoryFactoryBeanClass`属性。<br>
&emsp;&emsp;`BaseRepository`接口上文已经说明了作用。注意要在类头加上@NoRepositoryBean注解，
让Spring不要注入此类，手动使用`BaseRepositoryFactoryBean`的方式加入到Spring。<br>
&emsp;&emsp;在核心的`BaseRepositoryImpl`处理类中加入了一些自定义的方法。构造函数中通过参数获取实体的操作类。
接下来实现了一些可以使用原生sql来查询数据库的方法。<br>
&emsp;&emsp;`fakeDelete`方法实现了一个伪删除操作
（既：当表中存在state字段时，将值改为-1，不存在则执行删除，可根据个人情况修改`deleteWithState`方法），
该方法中操作数据的方式使用的是Criteria，有兴趣的朋友可以深入研究。<br>
&emsp;&emsp;重写了`save`方法，这个方法用于保存和修改（区别在于是否有id）。
重写的目的是判断标注了@DynamicUpdate注解的实体中的字段字段是否为空值，修改的时候会忽略修改空值的字段，
所以实体中数字型的字段类型一定要用大写的“类”型，如：Long、Integer。
### <a name="3">三、技巧篇</a><br>
&emsp;&emsp;项目中的Service可继承BaseService，用来减少一些重复性的代码，如简单的增删改查。<br>
&emsp;&emsp;项目中的Repository都继承了自定义的`BaseRepository`而不是默认的JpaRepository。<br>
&emsp;&emsp;`UserIdentifyRepository`中列出三种Jpa的查询方式。还有一种@NamedQuery方式因为要修改实体类，
所以本项目没有用到。<br>
&emsp;&emsp;`UserRepository`中展示类分页查询和返回自定义类的方法。<br>
### &emsp;&emsp;还有更多技巧希望大家补充！

