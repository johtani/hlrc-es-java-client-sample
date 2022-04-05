package info.johtani.sample.es.client.indexer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.johtani.sample.es.client.AbstractEsService;
import info.johtani.sample.es.client.Logger;
import info.johtani.sample.es.client.WikiDocument;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutComposableIndexTemplateRequest;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.List;

public class EsIndexService extends AbstractEsService {

    // JSONにして設定する必要があるため
    private static String toJsonString(WikiDocument doc) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(doc);
        } catch (JsonProcessingException e) {
            Logger.log("to json error...");
            e.printStackTrace();
        }
        return json;
    }

    public CreateTemplateResult createIndexTemplate(CreateTemplateRequest req) {
        CreateTemplateResult result = new CreateTemplateResult();

        try {
            PutComposableIndexTemplateRequest request = req.buildEsRequest();
            AcknowledgedResponse res = client.indices().putIndexTemplate(request, RequestOptions.DEFAULT);
            result.setError(!res.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public IndexResult bulkIndex(List<WikiDocument> docs, String indexName) throws IOException{
        IndexResult result = new IndexResult();
        BulkRequest request = new BulkRequest();
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        for (WikiDocument doc : docs) {
            // TODO とりあえず、登録処理だけ
            request.add(new IndexRequest(indexName).id(doc.getId()).source(toJsonString(doc), XContentType.JSON));
        }
        try {
            BulkResponse res = client.bulk(request, RequestOptions.DEFAULT);
            // FIXME error check
            result.setError(res.hasFailures());
            if (result.isError()) {
                Logger.log("Bulk indexing has some failures");
                // Easy way to write a combined message
                Logger.log(res.buildFailureMessage());
            } else {
                Logger.log("Bulk indexing success!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // FIXME log errors
            Logger.log(e.getMessage());
            throw e;
        }

        return result;
    }

    public AliasResult switchAlias(String name, String oldIndex, String newIndex) {
        AliasResult result = new AliasResult();
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        request.addAliasAction(new AliasActions(AliasActions.Type.ADD).index(newIndex).alias(name));
        request.addAliasAction(new AliasActions(AliasActions.Type.REMOVE).index(oldIndex).alias(name));
        try {
            AcknowledgedResponse res = client.indices().updateAliases(request, RequestOptions.DEFAULT);
            result.setError(!res.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean existsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    public DeleteResult deleteIndex(String indexName) throws IOException {
        DeleteResult result = new DeleteResult();
        DeleteIndexRequest request = new DeleteIndexRequest();
        request.indices(indexName);

        try {
            AcknowledgedResponse res = client.indices().delete(request, RequestOptions.DEFAULT);
            result.setError(!res.isAcknowledged());
            if(res.isAcknowledged() == false) {
                Logger.log("delete \"" + indexName + "\" index failed.");
            } else {
                result.setError(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public long countDocs(String indexName) throws IOException {
        long count = -1;
        CountRequest request = new CountRequest();
        request.indices(indexName);
        try {
            CountResponse res = client.count(request, RequestOptions.DEFAULT);
            count = res.getCount();
        } catch (IOException e) {
            // TODO 分かりやすくするためにトレース出しているだけ
            e.printStackTrace();
            throw e;
        }
        return count;
    }
}
