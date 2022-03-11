package info.johtani.sample.es.client;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class EsSearchRequest {

    //TODO support complex bool conditions
    private String searchText;

    //TODO support complex bool conditions
    private String fieldName;

    //TODO filter conditions

    //TODO sort condition

    public EsSearchRequest(String field, String searchText) {
        this.fieldName = field;
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    // たぶん、builderとかにして外に出すのがよさそう
    public SearchRequest buildEsSearchResult(EsSearchRequest request) {
        SearchRequest esRequest = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MatchQueryBuilder queryBuilder = new MatchQueryBuilder(request.getFieldName(), request.getSearchText());
        //query
        builder.query(queryBuilder);
        // TODO aggs

        // TODO sort option

        return esRequest.source(builder);
    }

    public String printQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.fieldName).append(":").append(this.searchText);
        return builder.toString();
    }
}
