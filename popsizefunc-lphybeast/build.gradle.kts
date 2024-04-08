plugins {
    `java-library`
    distribution
    `maven-publish`
    signing
    id("io.github.linguaphylo.platforms.lphy-publish") version "0.1.2"
}

// version has to be manually adjusted to keep same between version.xml and here
version = "0.0.1-SNAPSHOT" // -SNAPSHOT

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

val outDir = "${buildDir}/lphybeast"
val zippedConfig by configurations.creating

// if the project dependencies ues impl, then impl(proj(..)) will only have source code,
// which is equivalent to project-version.jar.
// if api is used, then all dependencies will pass to here,
// but do not use api unless you have to.
dependencies {
    //*** phylonco lphy + lphy ***//
    implementation(project(":popsizefunc-lphy"))

    //*** phylonco beast2 + beast2 + beastlab ***//
//    implementation(project(":popsizefunc-beast"))

    //*** lphybeast + ... ***//
    zippedConfig("io.github.linguaphylo:lphybeast:1.1.1-SNAPSHOT") // -SNAPSHOT
    // it must run installLPhyBEAST to unzip lphybeast.zip and create ${outDir}/lib,
    // the build is cleaned, or lphybeast version is renewed.
    implementation(fileTree("dir" to "${outDir}/lib", "include" to "**/*.jar"))
//    implementation(fileTree("dir" to "${outDir}/src", "include" to "**/*-sources.jar"))

    // add rest of lphybeast dependencies of beast2 part
    // non-modular lphy jar incl. all dependencies
    implementation( fileTree("lib") )

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
//    testRuntimeOnly(beast2)
}
tasks.compileJava.get().dependsOn("installLPhyBEAST")

// unzip lphybeast-*.zip to ${buildDir}/lphybeast/
val lb = tasks.register<Sync>("installLPhyBEAST") {
    zippedConfig.resolvedConfiguration.resolvedArtifacts.forEach({
        println(name + " --- unzip : " + it.file.name)
        if (it.file.name.endsWith("zip")) {
            from(zipTree(it.file))
            // why zipTree cannot provide files in root
            from(zipTree(it.file).matching({ include("**/version.xml") }).singleFile)
            into(outDir)
        }
    })
//    extra["lblibs"] = fileTree("dir" to "${outDir}/lib", "include" to "**/*.jar")
//    extra["lbsrc"] = fileTree("dir" to "${outDir}/src", "include" to "**/*-sources.jar")
}

// launch lphybeast
//tasks.register("runLPhyBEAST", JavaExec::class.java) {
//    // use classpath
//    jvmArgs = listOf("-cp", sourceSets.main.get().runtimeClasspath.asPath)
//    println("clspath = ${sourceSets.main.get().runtimeClasspath.asPath}")
//    mainClass.set("lphybeast.LPhyBeastCMD")
//    setArgs(listOf("-o", "$rootDir/tmp/gt16ErrMod.xml",
//        "${project(":phylonco-lphy").projectDir}/examples/gt16CoalErrModel.lphy"))
//}


tasks.jar {
    manifest {
        // shared attr in the root build
        attributes(
            "Implementation-Title" to "Population Size Function",
            "Implementation-Vendor" to "LPhy development team",
        )
    }
}

tasks.getByName<Tar>("distTar").enabled = false
// exclude start scripts
//tasks.getByName<CreateStartScripts>("startScripts").enabled = false

// dist as a beast2 package:
// 1. all released b2 packages are excluded;
// 2. lphy-*-all.jar is excluded, because SPI is not working with BEAST;
// 3. cannot use modular jar, because BEAST uses a customised non-module system.
distributions {
    main {
        distributionBaseName.set(project.base.archivesName.get())
        contents {
//            eachFile {  println(relativePath)  }
            includeEmptyDirs = false
            into("lib") {
                // include beast2 part
//                from(project(":popsizefunc-beast").tasks.jar)
                //TODO have to include lphy part, e.g. for lphybeast Unit tests
                from(project(":popsizefunc-lphy").tasks.jar)
                // include lphybeast part
                from(tasks.jar)
            }
            into("."){
                from("$projectDir"){
                    include("version.xml") }
                from("$rootDir") {
                    include("README.md")
                    include("LICENSE")
                }
            }
            // include src jar
            into("src") {
                from(tasks.getByName<Jar>("sourcesJar"))
//                from(project(":popsizefunc-beast").tasks.getByName<Jar>("sourcesJar"))
                from(project(":popsizefunc-lphy").tasks.getByName<Jar>("sourcesJar"))
            }
            into("examples") {
                from("$rootDir/examples")
//                from("${project(":popsizefunc-lphy").projectDir}/examples")
            }
//            into("fxtemplates") {
//                from("$rootDir/fxtemplates")
//            }
        }
    }
}

// beast 2 will remove version from Zip file name, and then decompress
// rm lphybeast-$version from the relative path of files inside Zip to make it working
tasks.withType<Zip>() {
    doFirst {
        if ( name.equals("distZip") ) {
            // only activate in distZip, otherwise will affect all jars and zips,
            // e.g. main class not found in lphybeast-$version.jar.
            eachFile {
                relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
                println(relativePath)
            }
        }
    }
}


publishing {
    publications {
        // project.name contains "lphy" substring
        create<MavenPublication>(project.name) {
            artifactId = project.base.archivesName.get()
            artifact(tasks.distZip.get())
            pom {
                description.set("LPhy to xml extension for Population Size Function")
                packaging = "zip"
                developers {
                    developer {
                        name.set("LPhy development team")
                    }
                }
            }
        }
    }
}

