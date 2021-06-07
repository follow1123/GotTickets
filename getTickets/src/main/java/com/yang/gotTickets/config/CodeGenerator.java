package com.yang.gotTickets.config;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.yang.gotTickets.bean.table.BaseEntity;
import com.yang.gotTickets.bean.table.TUser;
import com.yang.gotTickets.util.javassistUtils.CtClassBuilder;
import com.yang.gotTickets.util.javassistUtils.CtFiledBuilder;
import javassist.*;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.Consumer;

/**
 * 自动生成代码
 * 启动下面main函数,控制台提示输入module名、对应表名回车,表名前缀要求't_'
 * 注意点-> mapper接口手动加注解@Mapper
 * 官网-> https://mp.baomidou.com/guide/generator.html
 */
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StrUtil.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    private static String getAbsFileName(String projectPath, String pkg, TableInfo tableInfo, PackageConfig pc) {
        String nameSuffix = StrUtil.upperFirst(pkg);
        if (pkg.startsWith("bean")) nameSuffix = "";
        if (pkg.endsWith("impl")) {
            String[] strings = StrSpliter.split(pkg, "/", true, true).stream().map(StrUtil::upperFirst).toArray(String[]::new);
            nameSuffix = StrUtil.join("", (Object[]) strings);
        }

        return StrUtil.join("", projectPath, "/src/main/java/com/yang/", pc.getModuleName(), "/", pkg, "/", tableInfo.getEntityName(), nameSuffix, StringPool.DOT_JAVA);
    }

    public static void main(String[] args) {

        try {
            test01();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        String projectPath = System.getProperty("user.dir");
        // 全局配置
        mpg.setGlobalConfig(
                new GlobalConfig()
                        .setOutputDir(projectPath + "/src/main/java")
                        .setOpen(false)
                        .setSwagger2(true) //实体属性 Swagger2 注解
                        .setFileOverride(false)
                        .setActiveRecord(false)// 不需要ActiveRecord特性的请改为false
                        .setEnableCache(false)// XML 二级缓存
                        .setBaseResultMap(true)// XML ResultMap
                        .setBaseColumnList(false)// XML columList
                        .setAuthor("YF")// 作者
                        // 自定义文件命名，注意 %s 会自动填充表实体属性！
                        .setControllerName("%sController")
                        .setServiceName("%sService")
                        .setServiceImplName("%sServiceImpl")
                        .setMapperName("%sMapper")
                        .setXmlName("%sMapper")
        );


        // 数据源配置
        mpg.setDataSource(
                new DataSourceConfig()
                        .setDbType(DbType.MYSQL)
                        .setTypeConvert(new MySqlTypeConvert() {
                            @Override
                            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                                // 数据库内默认使用tinyint作为枚举字段类型，java内使用byte对应
                                if (fieldType.startsWith("tinyint")) {
                                    return DbColumnType.BYTE;
                                }
                                return super.processTypeConvert(globalConfig, fieldType);
                            }
                        })
                        .setUrl("jdbc:mysql://localhost:3306/got_tickets_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2b8")
                        .setDriverName("com.mysql.cj.jdbc.Driver")
                        .setUsername("root")
                        .setPassword("112358")
        );


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };

        PackageConfig pc = new PackageConfig()
                .setParent("com.yang")
                .setModuleName("generatorCodes")
                .setMapper("mapper")//dao
                .setService("service")//servcie
                .setController("controller")//controller
                .setEntity("bean.table");

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
// 自定义配置会被优先输出
        HashMap<String, String> map = new HashMap<String, String>() {{
            put("controller", "controller");
            put("bean/table", "entity");
            put("mapper", "mapper");
            put("service", "service");
            put("service/impl", "serviceImpl");
        }};
        String templateDir = "/crudTemplates/?.java.ftl";
        map.forEach((k, v) -> {
            focList.add(new FileOutConfig(templateDir.replace("?", v)) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return getAbsFileName(projectPath, k, tableInfo, pc);
                }
            });
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
//        设置自动生成的类导出的位置
        mpg.setPackageInfo( // 包配置
                pc
        );

        // 配置模板
        ;
        mpg.setTemplate(
                new TemplateConfig()
                        .setXml(null)
        );

        // 策略配置
        mpg.setStrategy(
                new StrategyConfig()
                        .setNaming(NamingStrategy.underline_to_camel)
                        .setColumnNaming(NamingStrategy.underline_to_camel)
                        .setEntityLombokModel(true)
                        .setRestControllerStyle(true)
                        .setInclude(scanner("表名，多个英文逗号分割").split(","))
                        .setControllerMappingHyphenStyle(true)
                        //过滤表名前缀
                        //.setTablePrefix("t_")
                        //指定继承类com.cloudnine.assess.bean.BaseEntity
                        .setSuperEntityClass(BaseEntity.class)
                        .setSuperServiceClass("com.yang.gotTickets.service.BaseService")
                        .setSuperServiceImplClass("com.yang.gotTickets.service.impl.BaseServiceImpl")
        );
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    //    @SneakyThrows
    private static void test01() throws Exception {

        List<String> strings = FileUtil.readLines(new File("D:\\Projects\\java\\idea\\my-project\\gotTickets\\src\\main\\java\\com\\yang\\gotTickets\\util\\AuthUtil.java"), "UTF-8");
        strings.forEach(System.out::println);


//        CtMethod ctMethod = new CtMethod();

        ClassPool pool = ClassPool.getDefault();

//        CtClass ctClass = pool.get(CodeGenerator.class.getName());
//
//        CtClass person = pool.makeClass("com.yang.config.Person");
//        // 创建构造器
////        CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get("java.lang.String"), CtClass.intType}, person);
////        person.addConstructor(constructor);
//
//        CtMethod method = CtMethod.make("public String getName(){return \"张三\";}", person);
//
//
//        person.addMethod(method);
//        CtField ctField = new CtField(CtClass.booleanType, "", person);
//        ctField.setModifiers(Modifier.PRIVATE);
//
//        String name = CodeGenerator.class.getName();
//        LinkedList<String> list = new LinkedList<>(StrSpliter.split(name, ".", true, true));
//        list.removeLast();
//        String pkgPath = ArrayUtil.join(list.toArray(new String[0]), "\\");
//        String absPath = StrUtil.join("\\", System.getProperty("user.dir"), pkgPath);
//
////        person.writeFile("C:\\Users\\YF\\Desktop\\source");
//        Class<?> aClass = person.toClass();
//        Method getName = aClass.getMethod("getName");
//        System.out.println(getName.invoke(aClass.newInstance()));
//        CtClass build = CtClassBuilder.builder()
//                .setName("person", CodeGenerator.class)
//                .addField(fb -> {
//                    fb.setTypeName(String.class, "name").build();
//                })
//                .addMethod(mb->{
//
//                })
//                .build();
    }
}
