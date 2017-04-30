package validator;

import java.util.ArrayList;
import java.util.List;

public class Validation {

    private List<String> errors;

    public Validation() {
        this.errors = new ArrayList<String>();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean isValid() {
        return (this.errors.isEmpty());
    }

    public String getErrorString() {
        String errorString = "";
        for (String error : errors) {
            errorString += error + ". ";
        }
        return errorString;
    }
}
