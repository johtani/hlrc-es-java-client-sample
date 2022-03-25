package info.johtani.sample.es.client.search;

import info.johtani.sample.es.client.Logger;
import info.johtani.sample.es.client.WikiDocument;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsSearchResult {

    private long totalHits = 0;
    private List<WikiDocument> results;

    public void fromEsResponse(SearchResponse response) {
        SearchHits hits = response.getHits();
        // count
        TotalHits totalHits = hits.getTotalHits();
        if (totalHits.relation != TotalHits.Relation.EQUAL_TO) Logger.log(" +++ Not Exact count!!");
        this.totalHits = totalHits.value;
        this.results = new ArrayList<>();

        // convert res to result
        for (SearchHit hit : hits.getHits()) {
            results.add(convertDoc(hit));
        }

        // convert facet to result
        //TODO
    }

    private WikiDocument convertDoc(SearchHit hit) {
        Map<String, Object> fields = hit.getSourceAsMap();
        WikiDocument doc = new WikiDocument(
                hit.getId(),
                (String) fields.get("title")
        );
        doc.setCategories((List) fields.get("categories"));
        doc.setHeadings((List) fields.get("headings"));
        doc.setRevision_id((String) fields.get("revision_id"));
        return doc;
    }

    public void printResults() {
        //TODO
        Logger.log("--- print result ---");
        Logger.log("hit count: " + this.totalHits);
        int count = 0;
        for (WikiDocument result : this.results) {
            count++;
            Logger.log("doc[" + count +"]");
            result.printDoc();
        }
    }
}
