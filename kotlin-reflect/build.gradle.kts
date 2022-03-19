import fr.xpdustry.toxopid.task.MindustryExec

dependencies {
    compileOnly(project(":kotlin-stdlib"))
    implementation(kotlin("reflect"))
}

tasks.withType(MindustryExec::class.java) {
    addArtifact(project(":kotlin-stdlib").tasks.shadowJar.get())
}
