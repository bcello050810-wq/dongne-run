export const sampleLocation = {
  latitude: 37.5284,
  longitude: 126.9331,
  label: '예시 위치 · 여의나루역',
};

// 화면과 계산을 확인하기 위한 예시 데이터
const sampleCourses = [
  {
    id: 1,
    name: '400m 트랙',
    type: 'loop',
    distanceMeters: 400,
    latitude: 37.5252,
    longitude: 126.9295,
    directions: '출발선을 기준으로 같은 방향으로 돌아요.',
  },
  {
    id: 2,
    name: '하천 산책로',
    type: 'out-and-back',
    distanceMeters: 1000,
    latitude: 37.5291,
    longitude: 126.9366,
    directions: '출발점에서 산책로를 따라간 뒤, 같은 길로 돌아와요.',
  },
];

function toRadians(degrees) {
  return (degrees * Math.PI) / 180;
}

function distanceBetween(start, end) {
  const startLatitude = toRadians(start.latitude);
  const endLatitude = toRadians(end.latitude);
  const latitudeDifference = endLatitude - startLatitude;
  const longitudeDifference = toRadians(end.longitude - start.longitude);

  // 두 좌표 사이의 직선거리를 m 단위로 근사한다.
  const a = Math.sin(latitudeDifference / 2) ** 2
    + Math.cos(startLatitude) * Math.cos(endLatitude) * Math.sin(longitudeDifference / 2) ** 2;

  return 2 * 6371000 * Math.asin(Math.sqrt(Math.min(a, 1)));
}

function describeRun(course, targetMeters) {
  const isLoop = course.type === 'loop';
  const repeatDistance = isLoop ? course.distanceMeters : course.distanceMeters * 2;
  const repeats = Math.floor(targetMeters / repeatDistance);
  const remainder = targetMeters % repeatDistance;

  const repeatText = repeats > 0 ? `${repeats}${isLoop ? '바퀴' : '왕복'}` : '';
  let remainderText = '';

  if (remainder > 0) {
    remainderText = isLoop ? `${remainder}m` : `${remainder / 2}m 지점에서 돌아오기`;
  }

  return [repeatText, remainderText].filter(Boolean).join(' + ');
}

export function recommendCourses(location, targetKm) {
  const targetMeters = targetKm * 1000;

  return sampleCourses
    .map((course) => ({
      ...course,
      distanceToStartMeters: distanceBetween(location, course),
      runPlan: describeRun(course, targetMeters),
      mapUrl: `https://map.kakao.com/link/map/${course.latitude},${course.longitude}`,
    }))
    .sort((first, second) => first.distanceToStartMeters - second.distanceToStartMeters);
}
