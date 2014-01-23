package net.dolpen.lib.data.input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSVとかをなんとかする
 */
public class LabeledRecord {
    public static class Builder {
        private List<String> labels;
        private String defaultValue = null;

        public Builder(List<String> labels) {
            if (labels == null || labels.isEmpty()) {
                throw new IllegalArgumentException("ラベルがありません");
            }
            this.labels = labels;
        }

        public Builder setDefault(String v) {
            defaultValue = v;
            return this;
        }

        public LabeledRecord build(List<String> values) {
            LabeledRecord r = new LabeledRecord();
            r.setDefault(defaultValue);
            int s = Math.min(labels.size(), values.size());
            for (int i = 0; i < s; i++)
                r.put(labels.get(i), values.get(i));

            return r;
        }
    }

    private Map<String, String> kv;
    private String defaultValue = null;

    private LabeledRecord() {
        kv = new HashMap<String, String>();
    }

    private void put(String k, String v) {
        kv.put(k, v);
    }

    private void setDefault(String v) {
        defaultValue = v;
    }

    public String get(String k) {
        String v = kv.get(k);
        return v != null ? v : defaultValue;
    }

}
