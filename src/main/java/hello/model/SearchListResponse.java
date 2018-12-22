package hello.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SearchListResponse
 */
@Validated

public class SearchListResponse   {
  @JsonProperty("lists")
  @Valid
  private List<SearchListResponseLists> lists = null;

  public SearchListResponse lists(List<SearchListResponseLists> lists) {
    this.lists = lists;
    return this;
  }

  public SearchListResponse addListsItem(SearchListResponseLists listsItem) {
    if (this.lists == null) {
      this.lists = new ArrayList<>();
    }
    this.lists.add(listsItem);
    return this;
  }

  /**
   * Get lists
   * @return lists
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<SearchListResponseLists> getLists() {
    return lists;
  }

  public void setLists(List<SearchListResponseLists> lists) {
    this.lists = lists;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchListResponse searchListResponse = (SearchListResponse) o;
    return Objects.equals(this.lists, searchListResponse.lists);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lists);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchListResponse {\n");
    
    sb.append("    lists: ").append(toIndentedString(lists)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

