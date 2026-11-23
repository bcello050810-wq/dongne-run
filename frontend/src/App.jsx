import { useState } from 'react';
import { recommendCourses, sampleLocation } from './courses.js';

const targetDistances = [3, 5, 10];

function CourseCard({ course }) {
  const isLoop = course.type === 'loop';
  const meters = course.distanceToStartMeters;
  const distance = meters < 1000 ? `${Math.round(meters)}m` : `${(meters / 1000).toFixed(1)}km`;

  return (
    <article className="course-card">
      <div className="course-heading">
        <h3>{course.name}</h3>
        <span className="course-type">{isLoop ? '순환형' : '왕복형'}</span>
      </div>
      <p className="course-meta">
        {isLoop ? '한 바퀴' : '편도'} {course.distanceMeters.toLocaleString('ko-KR')}m
        <span aria-hidden="true"> · </span>
        출발점까지 직선거리 {distance}
      </p>
      <p className="run-plan">{course.runPlan}</p>
      <p>{course.directions}</p>
      <a href={course.mapUrl} target="_blank" rel="noreferrer">카카오맵에서 예시 출발점 보기 ↗</a>
    </article>
  );
}

export default function App() {
  const [targetKm, setTargetKm] = useState(5);
  const [location, setLocation] = useState(sampleLocation);
  const [locating, setLocating] = useState(false);
  const [locationError, setLocationError] = useState('');
  const [recommendation, setRecommendation] = useState(null);

  function findCurrentLocation() {
    if (!navigator.geolocation) {
      setLocationError('이 브라우저에서는 현재 위치를 가져올 수 없어요. 예시 위치를 사용해 보세요.');
      return;
    }

    setLocating(true);
    setLocationError('');

    navigator.geolocation.getCurrentPosition(
      (position) => {
        setLocation({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
          label: '내 현재 위치',
        });
        setLocating(false);
      },
      (error) => {
        setLocationError(error.code === 1
          ? '위치 권한을 허용하면 현재 위치를 사용할 수 있어요. 예시 위치로도 추천할 수 있어요.'
          : '현재 위치를 가져오지 못했어요. 다시 시도하거나 예시 위치를 사용해 보세요.');
        setLocating(false);
      },
      { timeout: 8000 }
    );
  }

  function chooseSampleLocation() {
    setLocation(sampleLocation);
    setLocationError('');
  }

  function showRecommendations(event) {
    event.preventDefault();
    setRecommendation({
      targetKm,
      locationLabel: location.label,
      courses: recommendCourses(location, targetKm),
    });
  }

  return (
    <main className="home">
      <header className="hero">
        <h1>동네런</h1>
        <p>내 주변의 평지 러닝 코스를 찾고,<br />목표 거리만큼 달리는 방법을 알아보세요.</p>
      </header>

      <form className="search-panel" onSubmit={showRecommendations}>
        <fieldset>
          <legend>추천 기준 위치</legend>
          <div className="location-actions">
            <button type="button" className="location-button" onClick={findCurrentLocation} disabled={locating}>
              {locating ? '위치 확인 중…' : '내 위치 사용'}
            </button>
            <button type="button" className="location-button" onClick={chooseSampleLocation} disabled={locating}>
              예시 위치 사용
            </button>
          </div>
          <p className="location-status" role="status">{location.label}</p>
          {locationError && <p className="location-error" role="alert">{locationError}</p>}
        </fieldset>

        <fieldset>
          <legend>목표 거리</legend>
          <div className="distance-options">
            {targetDistances.map((distance) => (
              <label key={distance}>
                <input
                  type="radio"
                  name="distance"
                  value={distance}
                  checked={targetKm === distance}
                  onChange={() => setTargetKm(distance)}
                />
                <span>{distance} km</span>
              </label>
            ))}
          </div>
        </fieldset>

        <button type="submit" className="recommend-button">코스 추천</button>
      </form>

      <div aria-live="polite">
        {recommendation && (
          <section className="results" aria-labelledby="results-title">
            <h2 id="results-title">{recommendation.targetKm}km 추천 결과</h2>
            <p className="result-note">
              {recommendation.locationLabel} 기준으로 가까운 순서예요.
              아래 코스는 화면 확인용 예시이며 거리와 출발점은 검증 전이에요.
            </p>
            {recommendation.courses.map((course) => <CourseCard key={course.id} course={course} />)}
          </section>
        )}
      </div>
    </main>
  );
}
