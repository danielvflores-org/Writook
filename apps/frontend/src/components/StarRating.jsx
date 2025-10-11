import { useState } from 'react';

// StarRating component for displaying and rating stories
export default function StarRating({ 
  rating = 0, 
  maxStars = 5, 
  onRate, 
  readOnly = false, 
  size = "w-5 h-5" 
}) {
  const [hoveredRating, setHoveredRating] = useState(0);
  const [isHovering, setIsHovering] = useState(false);

  const handleStarClick = (starValue) => {
    if (!readOnly && onRate) {
      onRate(starValue);
    }
  };

  const handleStarHover = (starValue) => {
    if (!readOnly) {
      setHoveredRating(starValue);
      setIsHovering(true);
    }
  };

  const handleMouseLeave = () => {
    setIsHovering(false);
    setHoveredRating(0);
  };

  const displayRating = isHovering ? hoveredRating : rating;

  return (
    <div 
      className={`flex items-center space-x-1 ${!readOnly ? 'cursor-pointer' : ''}`}
      onMouseLeave={handleMouseLeave}
    >
      {[...Array(maxStars)].map((_, index) => {
        const starValue = index + 1;
        const isFilled = starValue <= displayRating;
        const isPartiallyFilled = !Number.isInteger(displayRating) && 
                                 starValue === Math.ceil(displayRating) && 
                                 starValue > Math.floor(displayRating);

        return (
          <div key={index} className="relative">
            <svg
              className={`${size} ${
                isFilled 
                  ? 'text-yellow-400' 
                  : readOnly 
                    ? 'text-gray-300' 
                    : 'text-gray-300 hover:text-yellow-400'
              } transition-colors duration-150`}
              fill="currentColor"
              viewBox="0 0 20 20"
              onClick={() => handleStarClick(starValue)}
              onMouseEnter={() => handleStarHover(starValue)}
            >
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
            </svg>
            
            {/* Partial fill for decimal ratings */}
            {isPartiallyFilled && (
              <svg
                className={`${size} text-yellow-400 absolute top-0 left-0 overflow-hidden`}
                style={{ 
                  clipPath: `inset(0 ${100 - ((displayRating % 1) * 100)}% 0 0)` 
                }}
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
            )}
          </div>
        );
      })}
      
      {/* Rating display */}
      {readOnly && rating > 0 && (
        <span className="text-sm text-gray-600 ml-2">
          {rating.toFixed(1)}
        </span>
      )}
    </div>
  );
}