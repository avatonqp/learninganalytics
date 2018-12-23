package kosmoglou.antogkou.learninganalytics.Analytics;

public class AnalyticsHelper {
  public Analytic analytic_document;

  public AnalyticsHelper(){
    // empty constructor for firestore
  }

  public AnalyticsHelper(Analytic analytic_document) {
    this.analytic_document = analytic_document;
  }

  public Analytic getAnalytic_document() {
    return analytic_document;
  }
  public void setAnalytic_document(Analytic analytic_document) {
    this.analytic_document = analytic_document;
  }
}
