package com.usit.app.spring.exception;


public class FrameworkException extends RuntimeException {

    /**
	 *
	 */
	private static final long serialVersionUID = 2196497312200630423L;
	private String msgKey;
    private String nextView;
    private Object msgArg;
    private String msg;

    /**
     * <pre>
     * getMsgKey
     * </pre>
     * @param N/A
     * @return String
     */
    public String getMsgKey() {
        return this.msgKey;
    }

    /**
     * <pre>
     * setMsgKey
     * </pre>
     * @param String msgKey
     * @return N/A
     */
    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    /**
     * <pre>
     * getMsg
     * </pre>
     * @param N/A
     * @return String
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * <pre>
     * setMsg
     * </pre>
     * @param String msg
     * @return N/A
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * <pre>
     * getNextView
     * </pre>
     * @param N/A
     * @return String
     */
    public String getNextView() {
        return this.nextView;
    }

    /**
     * <pre>
     * setNextView
     * </pre>
     * @param String nextView
     * @return N/A
     */
    public void setNextView(String nextView) {
        this.nextView = nextView;
    }

    /**
     * <pre>
     * getMsgArg
     * </pre>
     * @param N/A
     * @return Object
     */
    public Object getMsgArg() {
        return this.msgArg;
    }

    /**
     * <pre>
     * setMsgArg
     * </pre>
     * @param Object msgArg
     * @return N/A
     */
    public void setMsgArg(Object msgArg) {
        this.msgArg = msgArg;
    }

    /**
     * <pre>
     * FrameworkException
     * </pre>
     * @param Throwable cause, String msgKey
     * @return N/A
     */
    public FrameworkException(Throwable cause, String msgKey) {
        initCause(cause);

        this.msgKey = msgKey;
    }

    /**
     * <pre>
     * FrameworkException
     * </pre>
     * @param String msgKey, String msg
     * @return N/A
     */
    public FrameworkException(String msgKey, String msg) {
        initCause(new Throwable());
        this.msgKey = msgKey;
        this.msg = msg;
    }

    /**
     * <pre>
     * FrameworkException
     * </pre>
     * @param String msgKey, String msg, boolean isTrError
     * @return N/A
     */
    public FrameworkException(String msgKey, String msg, boolean isTrError) {
        initCause(new Throwable());
        this.msgKey = msgKey;
        this.msg = msg;
    }

    /**
     * <pre>
     * FrameworkException
     * </pre>
     * @param String trId, String msgKey, String msg, boolean isTrError
     * @return N/A
     */
    public FrameworkException(String trId, String msgKey, String msg, boolean isTrError) {
        initCause(new Throwable());
        this.msgKey = msgKey;
        this.msg = msg;
    }

    /**
     * <pre>
     * FrameworkException
     * </pre>
     * @param Throwable cause, String msgKey, Object msgArg
     * @return N/A
     */
    public FrameworkException(Throwable cause, String msgKey, Object msgArg) {
        initCause(cause);

        this.msgKey = msgKey;
        this.msgArg = msgArg;
    }

    /**
     * <pre>
     * FrameworkException
     * </pre>
     * @param Throwable cause, String msgKey, Object msgArg, String nextView
     * @return N/A
     */
    public FrameworkException(Throwable cause, String msgKey, Object msgArg, String nextView) {
        initCause(cause);

        this.msgKey = msgKey;
        this.msgArg = msgArg;
        this.nextView = nextView;
    }

}
