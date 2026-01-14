
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

group = "com"
version = "1.7.10-NEO-Special-Fix"
val archivesBaseName = "RCsEffect"

tasks.jar {
    manifest {
        attributes(
            "Manifest-Version" to "1.0"
        )
    }

    archiveBaseName.set(archivesBaseName)
    archiveVersion.set(version.toString())
    archiveClassifier.set("")
    archiveExtension.set("jar")
}
