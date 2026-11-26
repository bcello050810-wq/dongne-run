package com.dongnerun.backend.course;

/**
 * 목표 거리를 채우기 위해 코스를 몇 번 돌거나 왕복해야 하는지 나타낸다.
 *
 * @param repeats         순환형은 바퀴 수, 왕복형은 왕복 횟수
 * @param remainderMeters 반복하고 남은 거리(m)
 * @param description     화면에 보여줄 안내 문구 (예: "12바퀴 + 200m")
 */
public record RunPlan(int repeats, int remainderMeters, String description) {

    /**
     * 순환형은 한 바퀴 거리, 왕복형은 편도 거리의 두 배를 한 번 반복하는 거리로 본다.
     * 왕복형에서 남은 거리는 갔다가 돌아와야 하므로 그 절반 지점에서 돌아오도록 안내한다.
     */
    public static RunPlan of(CourseType type, int courseDistanceMeters, int targetMeters) {
        int repeatDistance = type == CourseType.LOOP ? courseDistanceMeters : courseDistanceMeters * 2;
        int repeats = targetMeters / repeatDistance;
        int remainder = targetMeters % repeatDistance;

        String repeatText = repeats > 0 ? repeats + (type == CourseType.LOOP ? "바퀴" : "왕복") : "";
        String remainderText = "";
        if (remainder > 0) {
            remainderText = type == CourseType.LOOP
                    ? remainder + "m"
                    : (remainder / 2) + "m 지점에서 돌아오기";
        }

        String description = repeatText.isEmpty() || remainderText.isEmpty()
                ? repeatText + remainderText
                : repeatText + " + " + remainderText;

        return new RunPlan(repeats, remainder, description);
    }
}