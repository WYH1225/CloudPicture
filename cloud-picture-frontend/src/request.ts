import { message } from "ant-design-vue";
import axios from "axios";

const myAxios = axios.create({
    baseURL: 'http://localhost:8000',
    timeout: 60000,
    withCredentials: true,
});

// 添加请求拦截器
myAxios.interceptors.request.use(
    function (config) {
        // 在发送请求之前做些什么
        return config;
    }, function (error) {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 全局响应拦截器
myAxios.interceptors.response.use(
    function (response) {
        const { data } = response;
        // 未登录
        if (data.code === 40100) {
            // 不是获取用户信息的请求，并且用户目前不是已经在用户登录界面，则跳转到登陆界面
            if (
                !response.request.responseURL.includes('user/get/login') &&
                !window.location.pathname.includes('/user/login')
            ) {
                message.warning('请先登录');
                window.location.href = `/user/login?redirect=${window.location.href}`;
            }
        }
        return response;
    }, function (error) {
        // 超出 2xx 范围的状态码都会触发该函数。
        // 对响应错误做点什么
        return Promise.reject(error);
    }
);

export default myAxios;