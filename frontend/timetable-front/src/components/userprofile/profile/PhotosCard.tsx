// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React, { useEffect } from 'react';
import { Typography, ImageList, ImageListItem, Skeleton, Box } from '@mui/material';

import img1 from "../../../assets/images/products/document.jpg";
import ChildCard from "../../shared/ChildCard";

interface photoType {
  img: string;
  id: number;
}

const photos: photoType[] = [
  {
    img: img1,
    id: 1,
  },
  {
    img: img1,
    id: 2,
  },
  {
    img: img1,
    id: 3,
  }
];

const PhotosCard = () => {

  const [isLoading, setLoading] = React.useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
    }, 500);
    
    return () => clearTimeout(timer);
  }, []);

  return (
    <ChildCard>
      <Typography mb={2} variant="h4">Документы</Typography>
      <Typography >Здесь находятся документы, подтверждающие квалификацию преподавателя
      </Typography>
      <ImageList cols={3} gap={20}>
        {photos.map((photo) => (
          <Box key={photo.id}>
            {
              isLoading ? (
                <>
                  <Skeleton
                    variant="rectangular"
                    animation="wave"
                    width="100%"
                    height={93}
                    key={photo.id}
                  ></Skeleton>
                </>
              ) : (
                <ImageListItem key={photo.id}>
                  <img
                    srcSet={`${photo.img} 1x, ${photo.img} 2x`}
                    alt={photo.img}
                    loading="lazy"
                    style={{ borderRadius: 8 }}
                  />
                </ImageListItem>
              )}
          </Box>
        ))}
      </ImageList>
    </ChildCard >
  )
};

export default PhotosCard;
