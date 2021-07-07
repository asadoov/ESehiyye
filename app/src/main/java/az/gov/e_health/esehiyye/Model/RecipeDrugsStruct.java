package az.gov.e_health.esehiyye.Model;

import java.util.List;

public class RecipeDrugsStruct {

    public class GetDrugListbyRecipeIdResult{

        public List<NewRecipeStruct.Exception> Exceptions;

        public boolean IsSuccessfullyExecuted = false;

        public int CurrentPage;

        public List<NewRecipeStruct.Drug> Results;

        public int TotalCount;

        public int TotalPageCount;
    }
public GetDrugListbyRecipeIdResult GetDrugListbyRecipeIdResult;

}
