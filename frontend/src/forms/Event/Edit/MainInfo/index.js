import React from 'react';
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import LeftContainer from "../../../utils/components/Container/LeftContainer";
import RightContainer from "../../../utils/components/Container/RightContainer";
import CommonInfo from './CommonInfo';
import Photos from "./Photos";

const MainInfo = () => {
    return <ContainerColumn>
        <LeftContainer style={{width: '60%'}}>
            <CommonInfo/>
        </LeftContainer>
        <RightContainer style={{width: '40%'}}>
            <Photos/>
        </RightContainer>
    </ContainerColumn>;
};

export default MainInfo;