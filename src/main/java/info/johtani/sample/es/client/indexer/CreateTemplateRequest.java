package info.johtani.sample.es.client.indexer;

import org.elasticsearch.client.indices.PutComposableIndexTemplateRequest;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.cluster.metadata.Template;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

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
        XContentBuilder mappings = buildMappings();

        Template template = new Template(
                settings.build(),
                new CompressedXContent(BytesReference.bytes(mappings)),
                null
        );
        ComposableIndexTemplate indexTemplate = new ComposableIndexTemplate(
                indexPattern, template, null, null, null, null
        );
        request.indexTemplate(indexTemplate);
        return request;
    }

    private Settings.Builder buildSettings() {
        Settings.Builder builder = Settings.builder()
                .put("index.number_of_replicas", "0")
                .put("index.number_of_shards", "3")
                .put("index.refresh_interval", "0")
                ;
        return builder;
    }

    private XContentBuilder buildMappings() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "keyword");
                }
                builder.endObject();

                builder.startObject("revision_id");
                {
                    builder.field("type", "keyword");
                }
                builder.endObject();

                builder.startObject("timestamp");
                {
                    builder.field("type", "date");
                }
                builder.endObject();

                builder.startObject("title");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "kuromoji");
                    builder.startObject("fields");
                    {
                        builder.startObject("keyword");
                        {
                            builder.field("type", "keyword");
                            builder.field("ignore_above", "256");
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();

                builder.startObject("headings");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "kuromoji");
                    builder.startObject("fields");
                    {
                        builder.startObject("keyword");
                        {
                            builder.field("type", "keyword");
                            builder.field("ignore_above", "256");
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();

                builder.startObject("categories");
                {
                    builder.field("type", "keyword");
                }
                builder.endObject();

                builder.startObject("contents");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "kuromoji");
                    builder.startObject("fields");
                    {
                        builder.startObject("keyword");
                        {
                            builder.field("type", "keyword");
                            builder.field("ignore_above", "256");
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();

                builder.startObject("images");
                {
                    builder.field("type", "nested");
                    builder.startObject("properties");
                    {
                        builder.startObject("taget");
                        {
                            builder.field("type", "keyword");
                        }
                        builder.endObject();
                        builder.startObject("target_type");
                        {
                            builder.field("type", "keyword");
                        }
                        builder.endObject();
                        builder.startObject("text");
                        {
                            builder.field("type", "nested");
                            builder.startObject("properties");
                            {
                                builder.startObject("text");
                                {
                                    builder.field("type", "text");
                                    builder.field("analyzer", "kuromoji");
                                    builder.startObject("fields");
                                    {
                                        builder.startObject("keyword");
                                        {
                                            builder.field("type", "keyword");
                                            builder.field("ignore_above", "256");
                                        }
                                        builder.endObject();
                                    }
                                    builder.endObject();
                                }
                                builder.endObject();
                                builder.startObject("link_target");
                                {
                                    builder.field("type", "keyword");
                                }
                                builder.endObject();
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();

                builder.startObject("links");
                {
                    builder.field("type", "nested");
                    builder.startObject("properties");
                    {
                        builder.startObject("text");
                        {
                            builder.field("type", "text");
                            builder.field("analyzer", "kuromoji");
                            builder.startObject("fields");
                            {
                                builder.startObject("keyword");
                                {
                                    builder.field("type", "keyword");
                                    builder.field("ignore_above", "256");
                                }
                                builder.endObject();
                            }
                            builder.endObject();
                        }
                        builder.endObject();

                        builder.startObject("link_target");
                        {
                            builder.field("type", "keyword");
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        return builder;
    }
}
