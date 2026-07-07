repositories { mavenCentral(); google() }
configurations { create("kspConfig") }
dependencies { "kspConfig"("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:+") }
tasks.register("printKsp") { doLast { println(configurations["kspConfig"].resolve()) } }
