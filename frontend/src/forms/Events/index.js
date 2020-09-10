import React from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import RightContainer from "forms/utils/components/Container/RightContainer";

import Filter from "./Filter";
import Events from "./Events";

const EventsPage = () => <Container>
    <ContainerColumn>
        <LeftContainer style={{width: '20%'}}>
            <Filter/>
        </LeftContainer>
        <RightContainer style={{width: '80%'}}>
            <Events/>
        </RightContainer>
    </ContainerColumn>
</Container>;

export default EventsPage;