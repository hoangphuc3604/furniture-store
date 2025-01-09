import React from "react";
import { Button, Result } from "antd";

const ErrorPage = ({
  status = "404",
  title = "404",
  subTitle = "Sorry, the page you visited does not exist.",
  buttonText = "Back Home",
  onButtonClick = () => {},
}) => (
  <Result
    status={status}
    title={title}
    subTitle={subTitle}
    extra={
      <Button type="primary" onClick={onButtonClick}>
        {buttonText}
      </Button>
    }
  />
);

export default ErrorPage;
