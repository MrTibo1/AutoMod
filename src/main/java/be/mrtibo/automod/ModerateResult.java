package be.mrtibo.automod;

public class ModerateResult {

    private final Result result;
    private final String reason;

    public ModerateResult(Result result, String reason){
        this.result = result;
        this.reason = reason;
    }

    public Result getResult(){return result;}
    public String getReason(){return reason;}

}
