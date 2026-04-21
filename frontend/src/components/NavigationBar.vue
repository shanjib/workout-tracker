<template>
  <div class="nav-bar">
    <div class="logo-wrap">
      <div class="logo-text">
        <span class="logo-abbr">Workout Tracker</span>
        <span class="logo-sub">Track your Push/Pull/Leg workouts!</span>
      </div>
    </div>

    <h3 class="nav-bar-text">{{ props.title }}</h3>

    <div class="nav-right">
      <div v-if="showNothing"></div>
      <!-- Unauthenticated mode -->
      <button v-else-if="!isAuthenticated" @click="goToLogin">Log In</button>

      <!-- Authenticated mode -->
      <template v-else>
        <button class="hamburger" @click="toggleMenu" aria-label="Menu">
          <span></span>
          <span></span>
          <span></span>
        </button>
        <div v-if="menuOpen" class="dropdown">
          <template v-for="item in visibleMenuItems" :key="item.label">
            <button v-if="item.type === 'action'" @click="item.action">
              {{ item.label }}
            </button>
            <router-link v-else :to="item.to" @click="closeMenu">
              {{ item.label }}
            </router-link>
          </template>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from "vue";
import { signOut } from "aws-amplify/auth";
import router from "../router";
import { getCurrentUser } from "aws-amplify/auth";

const isAuthenticated = ref(false);

const props = withDefaults(defineProps<{
  title: string;
  showNothing?: boolean;
  showProfileItem?: boolean;
}>(), {
  showNothing: false,
  showProfileItem: true
});

// --- Menu item definitions ---
// To add a new item: add an entry here and a corresponding prop above if needed.
const allMenuItems = computed(() => [
  {
    label: "Profile",
    type: "link" as const,
    to: "/profile",
    visible: props.showProfileItem,
  },
  {
    label: "Log Out",
    type: "action" as const,
    action: submitLogout,
    visible: true, // always shown
  },
]);

const visibleMenuItems = computed(() => {
  return allMenuItems.value.filter((item) => item.visible);
});

// --- State ---
const menuOpen = ref(false);

function toggleMenu() {
  menuOpen.value = !menuOpen.value;
}

function closeMenu() {
  menuOpen.value = false;
}

function goToLogin() {
  router.push("/login");
}

async function submitLogout() {
  closeMenu();
  await signOut();
  await router.push("/");
}

// Close on outside click
function handleClickOutside(event: MouseEvent) {
  const navRight = document.querySelector(".nav-right");
  if (navRight && !navRight.contains(event.target as Node)) {
    closeMenu();
  }
}

onMounted(async () => {
  document.addEventListener("click", handleClickOutside)

  try {
    await getCurrentUser();
    isAuthenticated.value = true;
  } catch {
    isAuthenticated.value = false;
  }
});
onUnmounted(() => document.removeEventListener("click", handleClickOutside));
</script>

<style scoped>
.nav-bar {
  display: flex;
  align-items: center;
  position: relative;
  padding: 0 16px;
  margin: 12px;
}

.nav-bar-text {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin: 0;
}

.logo-wrap {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.logo-abbr {
  font-size: 22px;
  letter-spacing: 2px;
}

.logo-sub {
  font-size: 10px;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.nav-right {
  margin-left: auto;
  position: relative;
}

.hamburger {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 28px;
  height: 20px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.hamburger span {
  display: block;
  height: 2px;
  background: currentColor;
  border-radius: 2px;
}

.dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 160px;
  padding: 6px;
  z-index: 100;
}

.dropdown button,
.dropdown a {
  display: block;
  text-align: left;
  background: none;
  border: none;
  padding: 10px 14px;
  cursor: pointer;
  font-size: 14px;
  text-decoration: none;
}
</style>
