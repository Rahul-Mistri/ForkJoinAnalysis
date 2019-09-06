public class Vector {

    float x;
    float y;

    public Vector(){
        x=0;
        y=0;
    }
    public Vector(float x,float y){
        this.x=x;
        this.y=y;
    }

    public float getMag(){
        return (float) Math.sqrt((x*x)+(y*y));
    }

}
