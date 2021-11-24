package br.com.frontendproject.utils;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonUtils {

    private File file;

    public JsonUtils(final String filePath) {
        this.file = new File(filePath);
    }

    public String getValue(final String path) {
        Object object = null;
        String value = "";
        try {
            final String json = FileUtils.readFileToString(this.file, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);

            final String[] array = this.getPath(path);
            for (final String param : array) {
                object = jsonObject.get(param);
                value = object.toString();
                jsonObject = new JSONObject(value);
            }
        } catch (final IOException | JSONException e) {
        }
        return value;
    }

    public void setValue(final String variable, final String newValue) {
        try {
            final Path path = Paths.get(this.file.getPath());
            final List<String> linhas = Files.readAllLines(path);

            String novoConteudo = "";
            for (int count = 0; count < linhas.size(); count++) {
                if (linhas.get(count).contains(variable)) {
                    novoConteudo = linhas.get(count).replaceAll("\\d+", newValue);
                    linhas.remove(count);
                    linhas.add(count, novoConteudo);
                }
            }
            Files.write(path, linhas);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getPath(final String arrayPath) {
        final String path = arrayPath.replace(".", ",");
        String[] array = path.split(",");
        if (array.length == 0) {
            array = new String[1];
            array[0] = path;
        }
        return array;
    }
}
