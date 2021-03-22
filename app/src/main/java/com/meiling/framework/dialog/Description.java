package com.meiling.framework.dialog;

public interface Description {
    /**
     * todo 对话框框架的基类，原本考虑在原有Dialog的module的基础上进行修改，但发现使用Google的databinding框架无法
     *  进行跨module来访问（即：layout生成的DataBinding对象无法访问到对应的布局文件中对应View组件）
     *  折中考虑后将框架基类放到启动module类中供使用
     */
}
