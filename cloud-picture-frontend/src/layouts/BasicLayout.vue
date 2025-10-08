<template>
    <div id="basicLayout">
        <a-layout style="min-height: 100vh">
            <a-layout-header class="header">
                <GlobalHeader />
            </a-layout-header>
            <a-layout-content class="content">
                <router-view />
            </a-layout-content>
            <a-layout-footer class="footer">
                云图库 by wyh
            </a-layout-footer>
        </a-layout>
    </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue';
</script>

<style scoped>
#basicLayout .header {
    margin-inline: 20px;
    background: white;
    color: unset;
    margin-bottom: 16px;
}

#basicLayout .content {
    padding: 40px;
    background: linear-gradient(to right, #fefefe, #fff);
    margin-bottom: 20px;
}

#basicLayout .footer {
    background-color: #efefef;
    padding: 16px;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
}
</style>