package de.spinfo.uni_koeln.html_parser.data;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mainForm;
    private String lemma;
    private String lemmaUsage;
    private String definition;
    private String etimology;
    private String etimologyUsage;
    private String variations;
    private String variationsUsage;
    private String derivates;
    private String derivatesUsages;
    private String note;




    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getMainForm() {
        return mainForm;
    }

    public void setMainForm(String mainForm) {
        this.mainForm = mainForm;
    }


    public String getLemmaUsage() {
        return lemmaUsage;
    }


    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setLemmaUsage(String lemmaUsage) {
        this.lemmaUsage = lemmaUsage;
    }

    public String getEtimology() {
        return etimology;
    }

    public void setEtimology(String etimology) {
        this.etimology = etimology;
    }

    public String getEtimologyUsage() {
        return etimologyUsage;
    }

    public void setEtimologyUsage(String etimologyUsage) {
        this.etimologyUsage = etimologyUsage;
    }

    public String getVariations() {
        return variations;
    }

    public void setVariations(String variations) {
        this.variations = variations;
    }

    public String getVariationsUsage() {
        return variationsUsage;
    }

    public void setVariationsUsage(String variationsUsage) {
        this.variationsUsage = variationsUsage;
    }

    public String getDerivates() {
        return derivates;
    }

    public void setDerivates(String derivates) {
        this.derivates = derivates;
    }

    public String getDerivatesUsages() {
        return derivatesUsages;
    }

    public void setDerivatesUsages(String derivatesUsages) {
        this.derivatesUsages = derivatesUsages;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<entry n=\"");
        String[] l = mainForm.split("\\s+");
        buffer.append(l[0]);
        buffer.append("\"> ");
        buffer.append("\n");


        buffer.append("<form>");
        buffer.append("\n");
        buffer.append("<orth>");
        if (l.length > 1) {

            String l1 = l[1];
            l1 = l1.replaceAll("\\.", "");
            buffer.append(l1.toLowerCase(Locale.GERMANY));
        }

        buffer.append("</orth>");
        buffer.append("\n");


        buffer.append("</form>");
        buffer.append("\n");



        buffer.append("</entry>");
        buffer.append("\n");


        return buffer.toString();
    }
}
