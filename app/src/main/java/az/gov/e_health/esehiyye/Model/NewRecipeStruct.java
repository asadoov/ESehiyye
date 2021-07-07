package az.gov.e_health.esehiyye.Model;

import java.util.List;

public class NewRecipeStruct {
    public class Exception{

        public String code;

        public String description;
    }
    public class NewRecipe {
        public String DoctorName;
        public int Id;
        public String InstName;
        public String PatientFather;
        public String PatientFullName;
        public String PatientName;
        public String PatientSurName;
        public String Pin;
        public String RecipeDate;
        public int StatusId;
        public String StatusText;


    }
    public class Drug {

        public int Id;
        public int drugCount;
        public String drugDosage;
        public int drugDosageId;
        public String drugDuration;
        public int drugDurationCount;
        public int drugDurationId;
        public String drugGivenQuantity;
        public int drugId;
        public String drugName;
        public String drugNote;
        public int drugQuantity;
        public String drugRoute;
        public int drugRouteId;
        public String drugScheduled;
        public int drugScheduledId;
        public String pharmacist;
    }
    public class GetRecipeListResult{

        public List<Exception> Exceptions;

        public boolean IsSuccessfullyExecuted = false;

        public int CurrentPage;

        public List<NewRecipeStruct.NewRecipe> Results;

        public int TotalCount;

        public int TotalPageCount;
    }
    public GetRecipeListResult GetRecipeListResult;
}
