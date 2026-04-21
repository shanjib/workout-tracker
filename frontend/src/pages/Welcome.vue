<template>
  <NavigationBar title="Welcome" :showLoginButton="true" />

  <div class="welcome-page">
    <section class="hero">
      <h1>Train Smarter with Push / Pull / Legs</h1>
      <p>
        A simple, proven workout split designed to build strength and muscle
        efficiently — whether you're training 3, 5, or 6 days a week.
      </p>
      <button @click="goToLogin">Get Started</button>
    </section>

    <section class="explainer">
      <div class="split-card">
        <h2>Push</h2>
        <p>
          Chest, shoulders, and triceps. These muscles work together to push
          weight away from your body — think bench press, overhead press, and
          tricep dips.
        </p>
      </div>
      <div class="split-card">
        <h2>Pull</h2>
        <p>
          Back and biceps. These muscles pull weight toward your body — think
          rows, pull-ups, and curls. A strong pull day builds a thick, wide back.
        </p>
      </div>
      <div class="split-card">
        <h2>Legs</h2>
        <p>
          Quads, hamstrings, glutes, and calves. Squats, deadlifts, and lunges
          form the foundation of a legs day — the most demanding and rewarding
          session of the week.
        </p>
      </div>
    </section>

    <section class="cta">
      <h2>Ready to start tracking?</h2>
      <p>Log your workouts, track your progress, and stay consistent.</p>
      <button @click="goToLogin">Log In</button>
    </section>
  </div>
</template>

<script setup lang="ts">
import NavigationBar from "../components/NavigationBar.vue";
import router from "../router";

function goToLogin() {
  router.push("/login");
}
</script>

<style scoped>
.welcome-page {
  max-width: 860px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  gap: 64px;
}

.hero {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 16px;
  padding-top: 48px;
}

.explainer {
  display: flex;
  gap: 24px;
}

.split-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cta {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 16px;
  padding-bottom: 64px;
}

@media (max-width: 600px) {
  .explainer {
    flex-direction: column;
  }
}
</style>
