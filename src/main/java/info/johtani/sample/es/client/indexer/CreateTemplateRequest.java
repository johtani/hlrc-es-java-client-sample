package info.johtani.sample.es.client.indexer;

import org.elasticsearch.client.indices.PutComposableIndexTemplateRequest;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.cluster.metadata.Template;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTemplateRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PutComposableIndexTemplateRequest buildEsRequest() throws IOException {
        PutComposableIndexTemplateRequest request = new PutComposableIndexTemplateRequest();
        //TODO
        request.name(name);

        List<String> indexPattern = new ArrayList<>();
        indexPattern.add("wikipedia-*");

        // sample index settings/mappings from https://github.com/johtani/wiki-json-loader/blob/master/sample/elasticsearch/index_schema.json
        // settings
        Settings.Builder settings = this.buildSettings();

        // mappings
        String mappings = WikipediaMappings.mappingJson;

        Template template = new Template(
                settings.build(),
                new CompressedXContent(mappings),
                null
        );
        ComposableIndexTemplate indexTemplate = new ComposableIndexTemplate(
                indexPattern, template, null, null, null, null
        );
        request.indexTemplate(indexTemplate);
        return request;
    }

    private Settings.Builder buildSettings() {
        Settings.Builder builder = Settings.builder();

        return builder;
    }

}

class WikipediaMappings {

    static String mappingJson = "{\n" +
            "      \"properties\" : {\n" +
            "        \"id\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"revision_id\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"timestamp\": {\n" +
            "          \"type\": \"date\"\n" +
            "        },\n" +
            "        \"title\" : {\n" +
            "          \"type\" : \"text\",\n" +
            "          \"analyzer\": \"kuromoji\",\n" +
            "          \"fields\" : {\n" +
            "            \"keyword\" : {\n" +
            "              \"type\" : \"keyword\",\n" +
            "              \"ignore_above\" : 256\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"headings\" : {\n" +
            "          \"type\" : \"text\",\n" +
            "          \"analyzer\": \"kuromoji\",\n" +
            "          \"fields\" : {\n" +
            "            \"keyword\" : {\n" +
            "              \"type\" : \"keyword\",\n" +
            "              \"ignore_above\" : 256\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"categories\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"contents\" : {\n" +
            "          \"type\" : \"text\",\n" +
            "          \"analyzer\": \"kuromoji\",\n" +
            "          \"fields\" : {\n" +
            "            \"keyword\" : {\n" +
            "              \"type\" : \"keyword\",\n" +
            "              \"ignore_above\" : 256\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"images\": {\n" +
            "          \"type\": \"nested\",\n" +
            "           \"properties\": {\n" +
            "             \"taget\": {\n" +
            "               \"type\": \"keyword\"\n" +
            "             },\n" +
            "             \"target_type\": {\n" +
            "               \"type\": \"keyword\"\n" +
            "             },\n" +
            "             \"text\": {\n" +
            "               \"type\": \"nested\",\n" +
            "               \"properties\": {\n" +
            "                 \"text\": {\n" +
            "                   \"type\" : \"text\",\n" +
            "                   \"analyzer\": \"kuromoji\",\n" +
            "                   \"fields\" : {\n" +
            "                     \"keyword\" : {\n" +
            "                       \"type\" : \"keyword\",\n" +
            "                       \"ignore_above\" : 256\n" +
            "                     }\n" +
            "                   }\n" +
            "                 },\n" +
            "                 \"link_target\": {\n" +
            "                   \"type\": \"keyword\"\n" +
            "                 }\n" +
            "               }\n" +
            "             }\n" +
            "           }\n" +
            "        },\n" +
            "        \"links\": {\n" +
            "          \"type\": \"nested\",\n" +
            "          \"properties\": {\n" +
            "            \"text\": {\n" +
            "              \"type\" : \"text\",\n" +
            "              \"analyzer\": \"kuromoji\",\n" +
            "              \"fields\" : {\n" +
            "                \"keyword\" : {\n" +
            "                  \"type\" : \"keyword\",\n" +
            "                  \"ignore_above\" : 256\n" +
            "                }\n" +
            "              }\n" +
            "            },\n" +
            "            \"link_target\": {\n" +
            "              \"type\": \"keyword\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }";
}
