// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import React from 'react';

import { Card, CardHeader, CardContent, Divider } from '@mui/material';
import { useSelector } from "../../store/Store";
import { AppState } from "../../store/Store";

type Props = {
  title: string;
  children: JSX.Element | JSX.Element[];
};

const BaseCard = ({ title, children }: Props) => {
  const customizer = useSelector((state: AppState) => state.customizer);

  return (
    <Card
      sx={{ padding: 0 }}
      elevation={customizer.isCardShadow ? 9 : 0}
      variant={!customizer.isCardShadow ? 'outlined' : undefined}
    >
      <CardHeader title={title} />
      <Divider />
      <CardContent>{children}</CardContent>
    </Card>
  );
};

export default BaseCard;
