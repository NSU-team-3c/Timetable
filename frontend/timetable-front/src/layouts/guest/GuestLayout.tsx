import { Box, styled } from '@mui/material';
import { FC } from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../../components/header/guest/GuestHeader';

const MainWrapper = styled('div')(() => ({
    top: '0',
    display: 'flex',
    minHeight: '100vh',
    width: '100%',
}));

const PageWrapper = styled('div')(() => ({
    display: 'flex',
    flexGrow: 1,
    paddingBottom: '60px',
    flexDirection: 'column',
    zIndex: 1,
    backgroundColor: 'transparent',
}));

const GuestLayout: FC = () => {

    return (
        <>
            <MainWrapper>
                <PageWrapper
                    className="page-wrapper"
                    sx={{
                        backgroundColor: 'background.default',
                    }}
                >
                    {/* ------------------------------------------- */}
                    {/* Header */}
                    {/* ------------------------------------------- */}
                    <Header />
                    {/* PageContent */}
                        {/* ------------------------------------------- */}
                        {/* PageContent */}
                        {/* ------------------------------------------- */}
                        <Box
                            sx={{
                                minHeight: 'calc(100vh - 170px)',
                                bottom: 0,
                                top: 0,
                                width: '70%',
                                margin: '0 auto 0 auto',
                                paddingTop: '0',
                                borderRadius: 0,
                            }}
                        >
                            <Outlet />
                        </Box>
                </PageWrapper>
            </MainWrapper>
        </>
    );
};

export default GuestLayout;
