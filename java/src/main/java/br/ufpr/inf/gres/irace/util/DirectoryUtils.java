/*
 * Copyright 2016 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufpr.inf.gres.irace.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 */
public class DirectoryUtils {

    public static void prepareOutputDirectory(String dir) {
        if (Files.notExists(Paths.get(dir))) {
            recreateDirectory(dir);
        }
    }
        
    public static void recreateDirectory(String dir) {
        File experimentDirectory;
        experimentDirectory = new File(dir);

        if (experimentDirectory.exists()) {
            experimentDirectory.delete();
        }

        experimentDirectory.mkdirs();
    }
    
    public static void createDirectory(String dir) {
        File experimentDirectory;
        experimentDirectory = new File(dir);

        if (!experimentDirectory.exists()) {
            experimentDirectory.mkdirs();
        }       
    }
   
    public static List<File> getFromLevel(String startDir, Integer levels) {
        File[] dirsObj = new File(startDir).listFiles(File::isDirectory);
        List<File> files = Arrays.asList(dirsObj);

        for (int i = 1; i < levels; i++) {
            files = getFilesFromDir(files);
        }

        return files;
    }

    public static List<File> getFilesFromDir(List<File> files) {
        List<File> result = new ArrayList();

        for (File file : files) {
            File[] dirsObj = new File(file.getPath()).listFiles(File::isDirectory);
            result.addAll(Arrays.asList(dirsObj));
        }

        return result;
    }

    public static void deleteDirectory(String directory) throws IOException {
        deleteDirectory(Paths.get(directory));
    }

    public static void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                       @Override
                       public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                           Files.delete(file);
                           return FileVisitResult.CONTINUE;
                       }

                       @Override
                       public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                           Files.delete(dir);
                           return FileVisitResult.CONTINUE;
                       }

                   });
    }
}
