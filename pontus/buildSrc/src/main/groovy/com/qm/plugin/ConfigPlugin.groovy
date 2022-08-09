package com.qm.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class ConfigPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("Configuring Project Structure for $project.name")

        System.out.println("========================")
        System.out.println("hello gradle plugin!")


//        while (project.configurations.iterator().hasNext()) {
//            System.out.println("------ > " + project.configurations.iterator().next())
//        }

//         project.android.defaultConfig.


        System.out.println("========================")
    }


}