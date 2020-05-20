import React from 'react';

import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import RightContainer from "forms/utils/components/Container/RightContainer";
import CommonInfo from './CommonInfo';
import Photos from "./Photos";

const MainInfo = () => {
    return <ContainerColumn>
        <LeftContainer style={{width: 600}}>
            <CommonInfo/>
        </LeftContainer>
        <RightContainer style={{width: 400}}>
            <Photos/>
        </RightContainer>
    </ContainerColumn>;
};

export default MainInfo;