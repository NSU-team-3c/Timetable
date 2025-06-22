// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { Grid } from '@mui/material';
import PageContainer from "../../components/container/PageContainer";
import Questions from "../../components/pages/faq/Questions";
import StillQuestions from "../../components/pages/faq/StillQuestions";

const Faq = () => {
    return (
        <PageContainer title="Помощь" description="this is Faq page">
            <Grid container spacing={3}>
                <Grid item xs>
                    <Questions />
                    <StillQuestions />
                </Grid>
            </Grid>
        </PageContainer>
    );
};

export default Faq;

