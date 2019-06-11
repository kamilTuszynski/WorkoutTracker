package com.workouttracker.util;

public class RPEUtil {

    public static double getPercentOfRM(float rpe, int reps){
        double result = 0;
        if(rpe == 10 || rpe == 0){
            switch (reps){
                case 1:
                    result = 1.0;
                    break;
                case 2:
                    result = 0.955;
                    break;
                case 3:
                    result = 0.922;
                    break;
                case 4:
                    result = 0.892;
                    break;
                case 5:
                    result = 0.863;
                    break;
                case 6:
                    result = 0.837;
                    break;
                case 7:
                    result = 0.811;
                    break;
                case 8:
                    result = 0.786;
                    break;
                case 9:
                    result = 0.762;
                    break;
                case 10:
                    result = 0.739;
                    break;
                case 11:
                    result = 0.707;
                    break;
                case 12:
                    result = 0.68;
                    break;
            }

        }else if(rpe == 9.5){
            switch (reps){
                case 1:
                    result = 0.978;
                    break;
                case 2:
                    result = 0.939;
                    break;
                case 3:
                    result = 0.907;
                    break;
                case 4:
                    result = 0.878;
                    break;
                case 5:
                    result = 0.85;
                    break;
                case 6:
                    result = 0.824;
                    break;
                case 7:
                    result = 0.799;
                    break;
                case 8:
                    result = 0.774;
                    break;
                case 9:
                    result = 0.751;
                    break;
                case 10:
                    result = 0.723;
                    break;
                case 11:
                    result = 0.694;
                    break;
                case 12:
                    result = 0.667;
                    break;
            }

        }else if(rpe == 9){
            switch (reps){
                case 1:
                    result = 0.955;
                    break;
                case 2:
                    result = 0.922;
                    break;
                case 3:
                    result = 0.892;
                    break;
                case 4:
                    result = 0.863;
                    break;
                case 5:
                    result = 0.837;
                    break;
                case 6:
                    result = 0.811;
                    break;
                case 7:
                    result = 0.786;
                    break;
                case 8:
                    result = 0.762;
                    break;
                case 9:
                    result = 0.739;
                    break;
                case 10:
                    result = 0.707;
                    break;
                case 11:
                    result = 0.68;
                    break;
                case 12:
                    result = 0.653;
                    break;
            }

        }else if(rpe == 8.5){
            switch (reps){
                case 1:
                    result = 0.939;
                    break;
                case 2:
                    result = 0.907;
                    break;
                case 3:
                    result = 0.878;
                    break;
                case 4:
                    result = 0.85;
                    break;
                case 5:
                    result = 0.824;
                    break;
                case 6:
                    result = 0.799;
                    break;
                case 7:
                    result = 0.774;
                    break;
                case 8:
                    result = 0.751;
                    break;
                case 9:
                    result = 0.723;
                    break;
                case 10:
                    result = 0.694;
                    break;
                case 11:
                    result = 0.667;
                    break;
                case 12:
                    result = 0.64;
                    break;
            }

        }else if(rpe == 8){
            switch (reps){
                case 1:
                    result = 0.922;
                    break;
                case 2:
                    result = 0.892;
                    break;
                case 3:
                    result = 0.863;
                    break;
                case 4:
                    result = 0.837;
                    break;
                case 5:
                    result = 0.811;
                    break;
                case 6:
                    result = 0.786;
                    break;
                case 7:
                    result = 0.762;
                    break;
                case 8:
                    result = 0.739;
                    break;
                case 9:
                    result = 0.707;
                    break;
                case 10:
                    result = 0.68;
                    break;
                case 11:
                    result = 0.653;
                    break;
                case 12:
                    result = 0.626;
                    break;
            }

        }else if(rpe == 7.5){
            switch (reps){
                case 1:
                    result = 0.907;
                    break;
                case 2:
                    result = 0.878;
                    break;
                case 3:
                    result = 0.85;
                    break;
                case 4:
                    result = 0.824;
                    break;
                case 5:
                    result = 0.799;
                    break;
                case 6:
                    result = 0.774;
                    break;
                case 7:
                    result = 0.751;
                    break;
                case 8:
                    result = 0.723;
                    break;
                case 9:
                    result = 0.694;
                    break;
                case 10:
                    result = 0.667;
                    break;
                case 11:
                    result = 0.64;
                    break;
                case 12:
                    result = 0.613;
                    break;
            }

        }else if(rpe == 7){
            switch (reps){
                case 1:
                    result = 0.892;
                    break;
                case 2:
                    result = 0.863;
                    break;
                case 3:
                    result = 0.837;
                    break;
                case 4:
                    result = 0.811;
                    break;
                case 5:
                    result = 0.786;
                    break;
                case 6:
                    result = 0.762;
                    break;
                case 7:
                    result = 0.739;
                    break;
                case 8:
                    result = 0.707;
                    break;
                case 9:
                    result = 0.68;
                    break;
                case 10:
                    result = 0.653;
                    break;
                case 11:
                    result = 0.626;
                    break;
                case 12:
                    result = 0.599;
                    break;
            }

        }else if(rpe == 6.5){
            switch (reps){
                case 1:
                    result = 0.878;
                    break;
                case 2:
                    result = 0.85;
                    break;
                case 3:
                    result = 0.824;
                    break;
                case 4:
                    result = 0.799;
                    break;
                case 5:
                    result = 0.774;
                    break;
                case 6:
                    result = 0.751;
                    break;
                case 7:
                    result = 0.723;
                    break;
                case 8:
                    result = 0.694;
                    break;
                case 9:
                    result = 0.667;
                    break;
                case 10:
                    result = 0.64;
                    break;
                case 11:
                    result = 0.613;
                    break;
                case 12:
                    result = 0.586;
                    break;
            }

        }else if(rpe == 6){
            switch (reps){
                case 1:
                    result = 0.863;
                    break;
                case 2:
                    result = 0.837;
                    break;
                case 3:
                    result = 0.811;
                    break;
                case 4:
                    result = 0.786;
                    break;
                case 5:
                    result = 0.762;
                    break;
                case 6:
                    result = 0.739;
                    break;
                case 7:
                    result = 0.707;
                    break;
                case 8:
                    result = 0.68;
                    break;
                case 9:
                    result = 0.653;
                    break;
                case 10:
                    result = 0.626;
                    break;
                case 11:
                    result = 0.599;
                    break;
                case 12:
                    result = 0.56;
                    break;
            }

        }else{

        }

        return result;
    }

    public static double calculateE1RM(float rpe, int reps, float weight){
        double percent = getPercentOfRM(rpe, reps);
        if(percent == 0){
            return weight;
        }

        return weight / percent;
    }
}
