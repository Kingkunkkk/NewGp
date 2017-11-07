package kun.gp.been;

/**
 * Created by kun on 2017-05-15.
 */

public class IndexData {

    /**
     * ltt : 2017/05/15 11:00
     * pc : 25156.34
     * value : 25285.54
     * difference : 129.20
     * turnover : 33429820819
     * high : 25309.85
     * low : 25213.47
     */

    private String ltt;
    private String pc;
    private String value;
    private String difference;
    private String turnover;
    private String high;
    private String low;

    public String getLtt() {
        return ltt;
    }

    public void setLtt(String ltt) {
        this.ltt = ltt;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public class NewData{

        /**
         * IndexCode : INDU
         * Name : 紐約道瓊斯指數
         * Point : 20896.61
         * Difference : -22.81
         */

        private String IndexCode;
        private String Name;
        private String Point;
        private String Difference;

        public String getIndexCode() {
            return IndexCode;
        }

        public void setIndexCode(String IndexCode) {
            this.IndexCode = IndexCode;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPoint() {
            return Point;
        }

        public void setPoint(String Point) {
            this.Point = Point;
        }

        public String getDifference() {
            return Difference;
        }

        public void setDifference(String Difference) {
            this.Difference = Difference;
        }
    }
}
