public class Vector {

    float x;
    float y;
    /** creates a vector*/
    public Vector(){
        x=0;
        y=0;
    }
    /** creates a vector*/
    public Vector(float x,float y){
        this.x=x;
        this.y=y;
    }
    /** returns the magnitude of the vector*/
    public float getMag(){
        return (float) Math.sqrt((x*x)+(y*y));
    }

}
